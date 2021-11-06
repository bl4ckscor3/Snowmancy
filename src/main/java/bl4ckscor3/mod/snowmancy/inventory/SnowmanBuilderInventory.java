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
	public int getContainerSize()
	{
		return SLOTS;
	}

	@Override
	public boolean isEmpty()
	{
		return contents.isEmpty();
	}

	@Override
	public ItemStack getItem(int index)
	{
		return contents.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count)
	{
		return itemHandler.extractItem(index, count, false);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index)
	{
		ItemStack stack = getItem(index);

		if(!stack.isEmpty())
			setItem(index, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setItem(int index, ItemStack stack)
	{
		contents.set(index, stack);
	}

	@Override
	public int getMaxStackSize()
	{
		return 1;
	}

	@Override
	public void setChanged()
	{
		itemHandler.getTileEntity().setChanged();
	}

	@Override
	public boolean stillValid(PlayerEntity player)
	{
		return true;
	}

	@Override
	public void startOpen(PlayerEntity player) {}

	@Override
	public void stopOpen(PlayerEntity player) {}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack)
	{
		return true;
	}

	@Override
	public void clearContent()
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
