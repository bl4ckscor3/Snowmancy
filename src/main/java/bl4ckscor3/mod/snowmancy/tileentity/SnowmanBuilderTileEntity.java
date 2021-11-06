package bl4ckscor3.mod.snowmancy.tileentity;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.container.SnowmanBuilderContainer;
import bl4ckscor3.mod.snowmancy.inventory.SnowmanBuilderInventory;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SnowmanBuilderTileEntity extends BlockEntity implements TickableBlockEntity, MenuProvider
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
				CompoundTag tag = new CompoundTag();
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
	public void load(BlockState state, CompoundTag tag)
	{
		CompoundTag invTag = (CompoundTag)tag.get("SnowmanBuilderInventory");

		if(invTag != null)
		{
			for(int i = 0; i < inventory.getContents().size(); i++)
			{
				if(invTag.contains("Slot" + i))
					inventory.setItem(i, ItemStack.of((CompoundTag)invTag.get("Slot" + i)));
			}

			progress = tag.getByte("progress");
		}

		super.load(state, tag);
	}

	@Override
	public CompoundTag save(CompoundTag tag)
	{
		CompoundTag invTag = new CompoundTag();

		for(int i = 0; i < inventory.getContents().size(); i++)
		{
			invTag.put("Slot" + i, inventory.getItem(i).save(new CompoundTag()));
		}

		tag.put("SnowmanBuilderInventory", invTag);
		tag.putByte("progress", progress);
		return super.save(tag);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return new ClientboundBlockEntityDataPacket(worldPosition, 1, save(new CompoundTag()));
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
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
	public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player)
	{
		return new SnowmanBuilderContainer(windowId, level, worldPosition, inv);
	}

	@Override
	public Component getDisplayName()
	{
		return new TranslatableComponent(Snowmancy.SNOWMAN_BUILDER.getDescriptionId());
	}
}
