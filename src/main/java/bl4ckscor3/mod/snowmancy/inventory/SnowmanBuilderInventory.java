package bl4ckscor3.mod.snowmancy.inventory;

import bl4ckscor3.mod.snowmancy.tileentity.SnowmanBuilderTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SnowmanBuilderInventory implements IInventory
{
	public static final int SLOTS = 14;
	private NonNullList<ItemStack> contents = NonNullList.<ItemStack>withSize(SLOTS, ItemStack.EMPTY);
	public SnowmanBuilderItemHandler itemHandler;

	/**
	 * Sets up this inventory with the container
	 * @param te The container of this inventory
	 */
	public SnowmanBuilderInventory(SnowmanBuilderTileEntity te)
	{
		itemHandler = new SnowmanBuilderItemHandler(te);
	}

	@Override
	public int getSizeInventory()
	{
		return SLOTS;
	}

	@Override
	public boolean isEmpty()
	{
		return contents.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return contents.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return itemHandler.extractItem(index, count, false);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		ItemStack stack = getStackInSlot(index);

		if(!stack.isEmpty())
			setInventorySlotContents(index, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		contents.set(index, stack);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public void markDirty()
	{
		itemHandler.getTileEntity().markDirty();
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{
		return true;
	}

	@Override
	public void openInventory(PlayerEntity player) {}

	@Override
	public void closeInventory(PlayerEntity player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
	}

	@Override
	public void clear()
	{
		for(int i = 0; i < contents.size(); i++)
		{
			contents.set(i, ItemStack.EMPTY);
		}
	}

	/**
	 * @return This inventory's item handler
	 */
	public SnowmanBuilderItemHandler getItemHandler()
	{
		return itemHandler;
	}

	/*
	 * @return This inventory's contents (index 0 is slot 0, etc)
	 */
	public NonNullList<ItemStack> getContents()
	{
		return contents;
	}
}
