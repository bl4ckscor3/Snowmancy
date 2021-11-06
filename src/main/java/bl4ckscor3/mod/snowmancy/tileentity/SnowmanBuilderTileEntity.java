package bl4ckscor3.mod.snowmancy.tileentity;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.container.SnowmanBuilderContainer;
import bl4ckscor3.mod.snowmancy.inventory.SnowmanBuilderInventory;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.block.BlockState;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SnowmanBuilderTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider
{
	private SnowmanBuilderInventory inventory = new SnowmanBuilderInventory(this);
	private byte progress = 0;
	private final byte maxProgress = 8;
	private final LazyOptional<IItemHandler> inventoryHolder = LazyOptional.of(() -> inventory.getItemHandler());

	public SnowmanBuilderTileEntity()
	{
		super(Snowmancy.teTypeBuilder);
	}

	@Override
	public void tick()
	{
		if(canOperate())
		{
			for(int i = 0; i < inventory.getContainerSize() - 1; i++) //last slot is output
			{
				if(inventory.getItem(i).isEmpty())
				{
					inventory.getItemHandler().setStackInSlot(inventory.getContainerSize() - 1, ItemStack.EMPTY);
					resetProgress();
					return;
				}
			}

			if(!isCraftReady())
			{
				ItemStack stack = new ItemStack(Snowmancy.FROZEN_SNOWMAN);
				Item weapon = inventory.getItem(inventory.getContainerSize() - 2).getItem();
				CompoundNBT tag = new CompoundNBT();
				EnumAttackType attackType = (weapon == Items.BOW ? EnumAttackType.ARROW :
					(weapon == Items.EGG ? EnumAttackType.EGG :
						(weapon == Items.SNOWBALL ? EnumAttackType.SNOWBALL : EnumAttackType.HIT)));

				tag.putBoolean("goldenCarrot", inventory.getItem(1).getItem() == Items.GOLDEN_CARROT);
				tag.putString("attackType", attackType.name());
				tag.putFloat("damage", attackType == EnumAttackType.HIT && weapon instanceof SwordItem ? 4.0F + ((SwordItem)weapon).getDamage() : 0.0F);
				tag.putBoolean("evercold", inventory.getItem(0).getItem() == Snowmancy.EVERCOLD_ICE.asItem());
				stack.setTag(tag);
				inventory.getItemHandler().setStackInSlot(inventory.getContainerSize() - 1, stack);
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
		return !inventory.getItem(inventory.getContainerSize() - 1).isEmpty();
	}

	/**
	 * @return true if the machine can work in the current climate, false otherwhise
	 */
	public boolean canOperate()
	{
		int cooling = 0;
		float temperature = getLevel().getBiome(worldPosition).getBaseTemperature();
		boolean cold = temperature < 0.2F;
		boolean medium = temperature < 1.0F;
		boolean warm = temperature >= 1.0F;

		for(Direction facing : Direction.values())
		{
			if(getLevel().getBlockState(worldPosition.relative(facing)).getBlock() == Snowmancy.EVERCOLD_ICE)
				cooling++;
		}

		switch(cooling)
		{
			case 0: case 1: return cold;
			case 2: case 3: return cold || medium;
			default: return cold || medium || warm;
		}
	}

	/**
	 * Used server-side whenever the tile entity changes in a way that requires the client to know
	 */
	public void markDirtyClient()
	{
		setChanged();

		if(level != null)
			level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
	}

	@Override
	public void load(BlockState state, CompoundNBT tag)
	{
		CompoundNBT invTag = (CompoundNBT)tag.get("SnowmanBuilderInventory");

		if(invTag != null)
		{
			for(int i = 0; i < inventory.getContents().size(); i++)
			{
				if(invTag.contains("Slot" + i))
					inventory.setItem(i, ItemStack.of((CompoundNBT)invTag.get("Slot" + i)));
			}

			progress = tag.getByte("progress");
		}

		super.load(state, tag);
	}

	@Override
	public CompoundNBT save(CompoundNBT tag)
	{
		CompoundNBT invTag = new CompoundNBT();

		for(int i = 0; i < inventory.getContents().size(); i++)
		{
			invTag.put("Slot" + i, inventory.getItem(i).save(new CompoundNBT()));
		}

		tag.put("SnowmanBuilderInventory", invTag);
		tag.putByte("progress", progress);
		return super.save(tag);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(worldPosition, 1, save(new CompoundNBT()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		progress = pkt.getTag().getByte("progress");
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return inventoryHolder.cast();
		else return super.getCapability(cap, side);
	}

	/**
	 * @return This tile's inventory
	 */
	public SnowmanBuilderInventory getInventory()
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
		return new SnowmanBuilderContainer(windowId, level, worldPosition, inv);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(Snowmancy.SNOWMAN_BUILDER.getDescriptionId());
	}
}
