package bl4ckscor3.mod.snowmancy.container;

import java.util.ArrayList;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.container.components.RestrictedSlot;
import bl4ckscor3.mod.snowmancy.inventory.SnowmanBuilderInventory;
import bl4ckscor3.mod.snowmancy.tileentity.SnowmanBuilderTileEntity;
import bl4ckscor3.mod.snowmancy.util.IStackValidator;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SnowmanBuilderContainer extends Container
{
	public static final ArrayList<ItemStack> WEAPONS = new ArrayList<>();
	public SnowmanBuilderTileEntity te;

	/**
	 * Registers an item to be a wearable weapon for the snowman
	 * @param item The item to register
	 */
	public static void registerWeapon(Item item)
	{
		WEAPONS.add(new ItemStack(item));
	}

	public SnowmanBuilderContainer(int windowId, World world, BlockPos pos, PlayerInventory inv)
	{
		super(Snowmancy.cTypeSnowmanBuilder, windowId);

		te = (SnowmanBuilderTileEntity)world.getTileEntity(pos);

		IInventory teInv = te.getInventory();

		//player inventory
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				addSlot(new Slot(inv, 9 + j + i * 9, 8 + j * 18, 157 + i * 18));
			}
		}

		//player hotbar
		for(int i = 0; i < 9; i++)
		{
			addSlot(new Slot(inv, i, 8 + i * 18, 215));
		}

		IStackValidator coalValidator = (stack) -> stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL;
		IStackValidator snowValidator = (stack) -> stack.getItem() == Blocks.SNOW_BLOCK.asItem();

		int slot = 0;

		//hat slot (always index 0!!)
		addSlot(new RestrictedSlot(teInv, slot++, 80, 7, 1, (stack) -> stack.getItem() == Snowmancy.EVERCOLD_ICE.asItem() || (stack.getItem() instanceof ArmorItem && ((ArmorItem)stack.getItem()).getEquipmentSlot() == EquipmentSlotType.HEAD))); //allow any helmet
		//nose slot (always index 1!!)
		addSlot(new RestrictedSlot(teInv, slot++, 80, 28, 1, (stack) -> stack.getItem() == Items.CARROT || stack.getItem() == Items.GOLDEN_CARROT));
		//eye slots (left, right)
		addSlot(new RestrictedSlot(teInv, slot++, 59, 18, 1, coalValidator));
		addSlot(new RestrictedSlot(teInv, slot++, 101, 18, 1, coalValidator));
		//mouth slots (left to right)
		addSlot(new RestrictedSlot(teInv, slot++, 38, 38, 1, coalValidator));
		addSlot(new RestrictedSlot(teInv, slot++, 59, 49, 1, coalValidator));
		addSlot(new RestrictedSlot(teInv, slot++, 80, 49, 1, coalValidator));
		addSlot(new RestrictedSlot(teInv, slot++, 101, 49, 1, coalValidator));
		addSlot(new RestrictedSlot(teInv, slot++, 122, 38, 1, coalValidator));
		//body slots (top to bottom)
		addSlot(new RestrictedSlot(teInv, slot++, 80, 74, 1, snowValidator));
		addSlot(new RestrictedSlot(teInv, slot++, 80, 103, 1, snowValidator));
		addSlot(new RestrictedSlot(teInv, slot++, 80, 132, 1, snowValidator));
		//weapon slot (always second last slot!)
		addSlot(new RestrictedSlot(teInv, slot++, 105, 89, 1, (stack) -> {
			for(ItemStack weapon : WEAPONS)
			{
				if(stack.getItem() == weapon.getItem())
					return true;
			}

			return false;
		}));
		//output (always last slot!)
		addSlot(new RestrictedSlot(teInv, slot++, 152, 132, 1, (stack) -> false));
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickType, PlayerEntity player)
	{
		SnowmanBuilderInventory inv = te.getInventory();
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

				if(player instanceof ServerPlayerEntity && inv.getStackInSlot(inv.getSizeInventory() - 1).getTag().getBoolean("evercold"))
					Snowmancy.CRAFT_EVERCOLD_SNOWMAN.trigger((ServerPlayerEntity)player);

				return super.slotClick(slotId, dragType, clickType, player);
			}
			else return ItemStack.EMPTY;
		}
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index)
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
	public boolean canInteractWith(PlayerEntity player)
	{
		return true;
	}
}
