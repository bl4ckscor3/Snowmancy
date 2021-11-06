package bl4ckscor3.mod.snowmancy.container.components;

import bl4ckscor3.mod.snowmancy.util.IStackValidator;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RestrictedSlot extends Slot
{
	private int slotLimit;
	private IStackValidator itemValidator;

	/**
	 * @param slotLimit The max amount of items allowed in this slot
	 * @param itemValidator The validator to check if a stack is valid for this slot
	 */
	public RestrictedSlot(Container inventory, int index, int xPosition, int yPosition, int slotLimit, IStackValidator itemValidator)
	{
		super(inventory, index, xPosition, yPosition);

		this.slotLimit = slotLimit;
		this.itemValidator = itemValidator;
	}

	@Override
	public boolean mayPlace(ItemStack stack)
	{
		return itemValidator.isValid(stack);
	}

	@Override
	public int getMaxStackSize(ItemStack stack)
	{
		return slotLimit;
	}
}
