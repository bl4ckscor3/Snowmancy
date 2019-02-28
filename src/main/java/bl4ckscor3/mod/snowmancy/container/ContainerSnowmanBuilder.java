package bl4ckscor3.mod.snowmancy.container;

import java.util.ArrayList;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.container.components.SlotRestricted;
import bl4ckscor3.mod.snowmancy.inventory.InventorySnowmanBuilder;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import bl4ckscor3.mod.snowmancy.util.IStackValidator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

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
				addSlot(new Slot(playerInv, 9 + j + i * 9, 8 + j * 18, 157 + i * 18));
			}
		}

		//player hotbar
		for(int i = 0; i < 9; i++)
		{
			addSlot(new Slot(playerInv, i, 8 + i * 18, 215));
		}

		IStackValidator coalValidator = (stack) -> stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL;
		IStackValidator snowValidator = (stack) -> stack.getItem() == Blocks.SNOW_BLOCK.asItem();

		int slot = 0;

		//hat slot (always index 0!!)
		addSlot(new SlotRestricted(te.getInventory(), slot++, 80, 7, 1, (stack) -> stack.getItem() == Snowmancy.EVERCOLD_ICE.asItem() || (stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).getEquipmentSlot() == EntityEquipmentSlot.HEAD))); //allow any helmet
		//nose slot (always index 1!!)
		addSlot(new SlotRestricted(te.getInventory(), slot++, 80, 28, 1, (stack) -> stack.getItem() == Items.CARROT || stack.getItem() == Items.GOLDEN_CARROT));
		//eye slots (left, right)
		addSlot(new SlotRestricted(te.getInventory(), slot++, 59, 18, 1, coalValidator));
		addSlot(new SlotRestricted(te.getInventory(), slot++, 101, 18, 1, coalValidator));
		//mouth slots (left to right)
		addSlot(new SlotRestricted(te.getInventory(), slot++, 38, 38, 1, coalValidator));
		addSlot(new SlotRestricted(te.getInventory(), slot++, 59, 49, 1, coalValidator));
		addSlot(new SlotRestricted(te.getInventory(), slot++, 80, 49, 1, coalValidator));
		addSlot(new SlotRestricted(te.getInventory(), slot++, 101, 49, 1, coalValidator));
		addSlot(new SlotRestricted(te.getInventory(), slot++, 122, 38, 1, coalValidator));
		//body slots (top to bottom)
		addSlot(new SlotRestricted(te.getInventory(), slot++, 80, 74, 1, snowValidator));
		addSlot(new SlotRestricted(te.getInventory(), slot++, 80, 103, 1, snowValidator));
		addSlot(new SlotRestricted(te.getInventory(), slot++, 80, 132, 1, snowValidator));
		//weapon slot (always second last slot!)
		addSlot(new SlotRestricted(te.getInventory(), slot++, 105, 89, 1, (stack) -> {
			for(ItemStack weapon : WEAPONS)
			{
				if(stack.getItem() == weapon.getItem())
					return true;
			}

			return false;
		}));
		//output (always last slot!)
		addSlot(new SlotRestricted(te.getInventory(), slot++, 152, 132, 1, (stack) -> false));
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
				for(int i = 0; i < inv.getSizeInventory() - 1; i++) //remove all input items
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

				if(player instanceof EntityPlayerMP && te.getInventory().getStackInSlot(te.getInventory().getSizeInventory() - 1).getTag().getBoolean("evercold"))
					Snowmancy.CRAFT_EVERCOLD_SNOWMAN.trigger((EntityPlayerMP)player);

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
