package bl4ckscor3.mod.snowmancy.util;

import net.minecraft.item.ItemStack;

/**
 * Functional interface to check if an item stack is valid (e.g. for putting in a slot)
 */
@FunctionalInterface
public interface IStackValidator
{
	/**
	 * @param stack The stack to validate
	 * @return true if the stack is valid in this context, false otherwhise
	 */
	boolean isValid(ItemStack stack);
}
