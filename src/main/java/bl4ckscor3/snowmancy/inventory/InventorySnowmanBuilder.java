package bl4ckscor3.snowmancy.inventory;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class InventorySnowmanBuilder implements IInventory
{
	public static final int SLOTS = 13;
	private NonNullList<ItemStack> contents = NonNullList.<ItemStack>withSize(SLOTS, ItemStack.EMPTY);
	private ItemHandlerSnowmanBuilder itemHandler;

	/**
	 * Sets up this inventory with the container
	 * @param te The container of this inventory
	 */
	public InventorySnowmanBuilder(TileEntitySnowmanBuilder te)
	{
		itemHandler = new ItemHandlerSnowmanBuilder(te);
	}

	@Override
	public String getName()
	{
		return Snowmancy.SNOWMAN_BUILDER.getTranslationKey();
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(Snowmancy.SNOWMAN_BUILDER.getTranslationKey());
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
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount()
	{
		return 0;
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
	public ItemHandlerSnowmanBuilder getItemHandler()
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
