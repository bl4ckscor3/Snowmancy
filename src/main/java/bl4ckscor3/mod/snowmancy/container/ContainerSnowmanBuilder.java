package bl4ckscor3.mod.snowmancy.container;

import java.util.ArrayList;

import bl4ckscor3.mod.snowmancy.container.components.SlotRestricted;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import bl4ckscor3.mod.snowmancy.util.ISnowmanWearable;
import bl4ckscor3.mod.snowmancy.util.IStackValidator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ContainerSnowmanBuilder extends Container
{
	public static final ArrayList<ItemStack> WEAPONS = new ArrayList<>();

	/**
	 * Registers an item to be a wearable weapon for the snowman
	 * @param item The item to register
	 */
	public static void registerWeapon(Item item)
	{
		WEAPONS.add(new ItemStack(item));
	}

	public ContainerSnowmanBuilder(InventoryPlayer playerInv, TileEntitySnowmanBuilder te)
	{
		//player inventory
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(playerInv, 9 + j + i * 9, 8 + j * 18, 157 + i * 18));
			}
		}

		//player hotbar
		for(int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 215));
		}

		IStackValidator coalValidator = (stack) -> {
			return OreDictionary.itemMatches(new ItemStack(Items.COAL), stack, false);
		};
		IStackValidator snowValidator = (stack) -> {
			return OreDictionary.itemMatches(new ItemStack(Item.getItemFromBlock(Blocks.SNOW)), stack, false);
		};

		//hat slot
		addSlotToContainer(new SlotRestricted(te.getInventory(), 0, 80, 7, 1, (stack) -> {
			return stack.getItem() instanceof ISnowmanWearable;
		}));
		//left eye slot
		addSlotToContainer(new SlotRestricted(te.getInventory(), 1, 59, 18, 1, coalValidator));
		//nose slot
		addSlotToContainer(new SlotRestricted(te.getInventory(), 2, 80, 28, 1, (stack) -> {
			return OreDictionary.itemMatches(new ItemStack(Items.CARROT), stack, false);
		}));
		//right eye slot
		addSlotToContainer(new SlotRestricted(te.getInventory(), 3, 101, 18, 1, coalValidator));
		//mouth slots (left to right)
		addSlotToContainer(new SlotRestricted(te.getInventory(), 4, 38, 38, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), 5, 59, 49, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), 6, 80, 49, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), 7, 101, 49, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), 8, 122, 38, 1, coalValidator));
		//top body slots
		addSlotToContainer(new SlotRestricted(te.getInventory(), 9, 80, 74, 1, snowValidator));
		//weapon slot
		addSlotToContainer(new SlotRestricted(te.getInventory(), 10, 105, 89, 1, (stack) -> {
			for(ItemStack weapon : WEAPONS)
			{
				if(OreDictionary.itemMatches(weapon, stack, false))
					return true;
			}

			return false;
		}));
		//body slots (middle to bottom)
		addSlotToContainer(new SlotRestricted(te.getInventory(), 11, 80, 103, 1, snowValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), 12, 80, 132, 1, snowValidator));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
}
