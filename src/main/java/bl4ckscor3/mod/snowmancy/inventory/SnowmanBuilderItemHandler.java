package bl4ckscor3.mod.snowmancy.inventory;

import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class SnowmanBuilderItemHandler implements IItemHandlerModifiable {
	public SnowmanBuilderBlockEntity be;

	public SnowmanBuilderItemHandler(SnowmanBuilderBlockEntity be) {
		this.be = be;
	}

	@Override
	public int getSlots() {
		return SnowmanBuilderInventory.SLOTS;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return be.getInventory().getItem(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stackToInsert, boolean simulate) {
		if (stackToInsert.isEmpty() || slot < 0 || slot >= getSlots())
			return stackToInsert;

		ItemStack slotStack = getStackInSlot(slot);
		int limit = getSlotLimit(slot);

		if (slotStack.isEmpty()) {
			if (!simulate)
				setStackInSlot(slot, stackToInsert);

			return ItemStack.EMPTY;
		}
		else if (areItemStacksEqual(slotStack, stackToInsert) && slotStack.getCount() < limit) {
			if (limit - slotStack.getCount() >= stackToInsert.getCount()) {
				if (!simulate)
					slotStack.setCount(slotStack.getCount() + stackToInsert.getCount());

				return ItemStack.EMPTY;
			}
			else {
				ItemStack toInsert = stackToInsert.copy();
				ItemStack toReturn = toInsert.split((slotStack.getCount() + stackToInsert.getCount()) - limit); //this is the remaining stack that could not be inserted

				if (!simulate)
					slotStack.setCount(slotStack.getCount() + toInsert.getCount());

				return toReturn;
			}
		}

		return stackToInsert;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack stack = getStackInSlot(slot);

		if (stack.isEmpty() || slot < 0 || slot >= getSlots() || amount < 1)
			return ItemStack.EMPTY;
		else if (amount >= stack.getCount()) {
			if (!simulate)
				setStackInSlot(slot, ItemStack.EMPTY);

			return stack.copy();
		}
		else {
			if (!simulate)
				stack.shrink(amount);

			return stack.copy().split(amount);
		}
	}

	@Override
	public int getSlotLimit(int slot) {
		ItemStack stack = getStackInSlot(slot);

		return stack.isEmpty() ? 64 : stack.getItem().getMaxStackSize(stack);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		be.getInventory().getContents().set(slot, stack);
	}

	/**
	 * @return The TileEntity that is bound to the inventory whith this item handler
	 */
	public SnowmanBuilderBlockEntity getBlockEntity() {
		return be;
	}

	/**
	 * See {@link ItemStack#areItemStacksEqual(ItemStack, ItemStack)} but without size restriction
	 */
	public static boolean areItemStacksEqual(ItemStack stack1, ItemStack stack2) {
		ItemStack s1 = stack1.copy();
		ItemStack s2 = stack2.copy();

		s1.setCount(1);
		s2.setCount(1);
		return ItemStack.matches(s1, s2);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return true;
	}
}
