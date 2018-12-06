package bl4ckscor3.mod.snowmancy.container;

import java.util.ArrayList;

import bl4ckscor3.mod.snowmancy.container.components.SlotRestricted;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import bl4ckscor3.mod.snowmancy.util.ISnowmanWearable;
import bl4ckscor3.mod.snowmancy.util.IStackValidator;
import bl4ckscor3.snowmancy.inventory.InventorySnowmanBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ContainerSnowmanBuilder extends Container
{
	public static final ArrayList<ItemStack> WEAPONS = new ArrayList<>();
	private TileEntitySnowmanBuilder te;

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
		this.te = te;

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

		int slot = 0;

		//hat slot
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 80, 7, 1, (stack) -> {
			return stack.getItem() instanceof ISnowmanWearable || stack.getItem() == Items.IRON_HELMET; //TODO don't forget debug
		}));
		//nose slot
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 80, 28, 1, (stack) -> {
			return OreDictionary.itemMatches(new ItemStack(Items.CARROT), stack, false);
		}));
		//eye slots (left, right)
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 59, 18, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 101, 18, 1, coalValidator));
		//mouth slots (left to right)
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 38, 38, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 59, 49, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 80, 49, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 101, 49, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 122, 38, 1, coalValidator));
		//body slots (top to bottom)
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 80, 74, 1, snowValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 80, 103, 1, snowValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 80, 132, 1, snowValidator));
		//weapon slot
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 105, 89, 1, (stack) -> {
			for(ItemStack weapon : WEAPONS)
			{
				if(OreDictionary.itemMatches(weapon, stack, false))
					return true;
			}

			return false;
		}));
		//output (always last slot!)
		addSlotToContainer(new SlotRestricted(te.getInventory(), slot++, 152, 132, 1, (stack) -> {return false;}));
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player)
	{
		InventorySnowmanBuilder inv = te.getInventory();
		boolean clickedOutput = false;

		if(slotId == 36 + inv.getSizeInventory() - 1 && !inv.getStackInSlot(inv.getSizeInventory() - 1).isEmpty()) //last slot
		{
			clickedOutput = true;

			if(te.getProgress() == te.getMaxProgress())
			{
				for(int i = 0; i < inv.getSizeInventory() - 1; i++)
				{
					inv.getItemHandler().extractItem(i, 1, false);
				}
			}
		}

		if(!clickedOutput)
			return super.slotClick(slotId, dragType, clickType, player);
		else
		{
			if(te.getProgress() == te.getMaxProgress())
			{
				te.resetProgress();
				return super.slotClick(slotId, dragType, clickType, player);
			}
			else return ItemStack.EMPTY;
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack copy = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if(slot != null && slot.getHasStack())
		{
			ItemStack slotStack = slot.getStack();

			copy = slotStack.copy();

			if(index <= 35)
			{
				if(!mergeItemStack(slotStack, 36, 36 + te.getInventory().getSizeInventory(), false))
					return ItemStack.EMPTY;
			}
			else if(index >= 36)
			{
				if(!mergeItemStack(slotStack, 0, 36, false))
					return ItemStack.EMPTY;
			}

			if(slotStack.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
		}

		return copy;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
}
