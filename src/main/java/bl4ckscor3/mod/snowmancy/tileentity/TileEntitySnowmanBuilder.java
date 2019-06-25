package bl4ckscor3.mod.snowmancy.tileentity;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.container.ContainerSnowmanBuilder;
import bl4ckscor3.mod.snowmancy.inventory.InventorySnowmanBuilder;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntitySnowmanBuilder extends TileEntity implements ITickableTileEntity, INamedContainerProvider
{
	private InventorySnowmanBuilder inventory = new InventorySnowmanBuilder(this);
	private byte progress = 0;
	private final byte maxProgress = 8;
	private final LazyOptional inventoryHolder = LazyOptional.of(() -> inventory.getItemHandler());

	public TileEntitySnowmanBuilder()
	{
		super(Snowmancy.teTypeBuilder);
	}

	@Override
	public void tick()
	{
		if(canOperate())
		{
			for(int i = 0; i < inventory.getSizeInventory() - 1; i++) //last slot is output
			{
				if(inventory.getStackInSlot(i).isEmpty())
				{
					inventory.getItemHandler().setStackInSlot(inventory.getSizeInventory() - 1, ItemStack.EMPTY);
					resetProgress();
					return;
				}
			}

			if(!isCraftReady())
			{
				ItemStack stack = new ItemStack(Snowmancy.FROZEN_SNOWMAN);
				Item weapon = inventory.getStackInSlot(inventory.getSizeInventory() - 2).getItem();
				CompoundNBT tag = new CompoundNBT();
				EnumAttackType attackType = (weapon == Items.BOW ? EnumAttackType.ARROW :
					(weapon == Items.EGG ? EnumAttackType.EGG :
						(weapon == Items.SNOWBALL ? EnumAttackType.SNOWBALL : EnumAttackType.HIT)));

				tag.putBoolean("goldenCarrot", inventory.getStackInSlot(1).getItem() == Items.GOLDEN_CARROT);
				tag.putString("attackType", attackType.name());
				tag.putFloat("damage", attackType == EnumAttackType.HIT && weapon instanceof SwordItem ? 4.0F + ((SwordItem)weapon).getAttackDamage() : 0.0F);
				tag.putBoolean("evercold", inventory.getStackInSlot(0).getItem() == Snowmancy.EVERCOLD_ICE.asItem());
				stack.setTag(tag);
				inventory.getItemHandler().setStackInSlot(inventory.getSizeInventory() - 1, stack);
			}
		}
	}

	/**
	 * Increases the progress of the current craft (if applicable) by one
	 */
	public void increaseProgress()
	{
		if(isCraftReady() && progress < maxProgress)
		{
			progress++;
			markDirtyClient();
		}
	}

	/**
	 * Resets the progress of the ongoing craft, probably because it got aborted or was finished
	 */
	public void resetProgress()
	{
		progress = 0;
		markDirtyClient();
	}

	/**
	 * Checks if the crafting status is ready by checking if the last slot (output slot) of this tile's inventory is not empty
	 * @return true if the crafting status is ready, false otherwhise
	 */
	public boolean isCraftReady()
	{
		return !inventory.getStackInSlot(inventory.getSizeInventory() - 1).isEmpty();
	}

	/**
	 * @return true if the machine can work in the current climate, false otherwhise
	 */
	public boolean canOperate()
	{
		int cooling = 0;
		boolean cold = getWorld().getBiome(pos).getTempCategory() == TempCategory.COLD;
		boolean medium = getWorld().getBiome(pos).getTempCategory() == TempCategory.MEDIUM;
		boolean ocean = getWorld().getBiome(pos).getTempCategory() == TempCategory.OCEAN;
		boolean warm = getWorld().getBiome(pos).getTempCategory() == TempCategory.WARM;

		for(Direction facing : Direction.values())
		{
			if(getWorld().getBlockState(pos.offset(facing)).getBlock() == Snowmancy.EVERCOLD_ICE)
				cooling++;
		}

		switch(cooling)
		{
			case 0: case 1: return cold;
			case 2: case 3: return cold || medium || ocean;
			default: return cold || medium || ocean || warm;
		}
	}

	/**
	 * Used server-side whenever the tile entity changes in a way that requires the client to know
	 */
	public void markDirtyClient()
	{
		markDirty();

		if(world != null)
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}

	@Override
	public void read(CompoundNBT tag)
	{
		CompoundNBT invTag = (CompoundNBT)tag.get("SnowmanBuilderInventory");

		if(invTag != null)
		{
			for(int i = 0; i < inventory.getContents().size(); i++)
			{
				if(invTag.contains("Slot" + i))
					inventory.setInventorySlotContents(i, ItemStack.read((CompoundNBT)invTag.get("Slot" + i)));
			}

			progress = tag.getByte("progress");
		}

		super.read(tag);
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		CompoundNBT invTag = new CompoundNBT();

		for(int i = 0; i < inventory.getContents().size(); i++)
		{
			invTag.put("Slot" + i, inventory.getStackInSlot(i).write(new CompoundNBT()));
		}

		tag.put("SnowmanBuilderInventory", invTag);
		tag.putByte("progress", progress);
		return super.write(tag);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(pos, 1, write(new CompoundNBT()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		progress = pkt.getNbtCompound().getByte("progress");
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap)
	{
		return cap.orEmpty(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inventoryHolder);
	}

	/**
	 * @return This tile's inventory
	 */
	public InventorySnowmanBuilder getInventory()
	{
		return inventory;
	}

	/**
	 * Gets the crafting progress
	 * @return The crafting progress
	 */
	public byte getProgress()
	{
		return progress;
	}

	/**
	 * Gets the crafting progress' maximum progress (upon which the craft will be completed)
	 * @return The crafting progress' maximum progress
	 */
	public byte getMaxProgress()
	{
		return maxProgress;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player)
	{
		return new ContainerSnowmanBuilder(windowId, world, pos, inv);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(Snowmancy.SNOWMAN_BUILDER.getTranslationKey());
	}
}
