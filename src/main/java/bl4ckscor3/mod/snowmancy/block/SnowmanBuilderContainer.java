package bl4ckscor3.mod.snowmancy.block;

import java.util.ArrayList;
import java.util.function.Predicate;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.inventory.RestrictedSlot;
import bl4ckscor3.mod.snowmancy.inventory.SnowmanBuilderInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class SnowmanBuilderContainer extends AbstractContainerMenu {
	public static final ArrayList<ItemStack> WEAPONS = new ArrayList<>();
	public SnowmanBuilderBlockEntity be;

	/**
	 * Registers an item to be a wearable weapon for the snowman
	 *
	 * @param item The item to register
	 */
	public static void registerWeapon(Item item) {
		WEAPONS.add(new ItemStack(item));
	}

	public SnowmanBuilderContainer(int windowId, Level level, BlockPos pos, Inventory inv) {
		super(Snowmancy.SNOWMAN_BUILDER_MENU.get(), windowId);

		be = (SnowmanBuilderBlockEntity) level.getBlockEntity(pos);

		Container beInv = be.getInventory();

		//player inventory
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlot(new Slot(inv, 9 + j + i * 9, 8 + j * 18, 157 + i * 18));
			}
		}

		//player hotbar
		for (int i = 0; i < 9; i++) {
			addSlot(new Slot(inv, i, 8 + i * 18, 215));
		}

		Predicate<ItemStack> coalValidator = stack -> stack.is(Items.COAL) || stack.is(Items.CHARCOAL);
		Predicate<ItemStack> snowValidator = stack -> stack.is(Blocks.SNOW_BLOCK.asItem());

		int slot = 0;

		//hat slot (always index 0!!)
		addSlot(new RestrictedSlot(beInv, slot++, 80, 7, 1, stack -> stack.is(Snowmancy.EVERCOLD_ICE.get().asItem()) || (stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getEquipmentSlot() == EquipmentSlot.HEAD))); //allow any helmet
		//nose slot (always index 1!!)
		addSlot(new RestrictedSlot(beInv, slot++, 80, 28, 1, stack -> stack.is(Items.CARROT) || stack.is(Items.GOLDEN_CARROT)));
		//eye slots (left, right)
		addSlot(new RestrictedSlot(beInv, slot++, 59, 18, 1, coalValidator));
		addSlot(new RestrictedSlot(beInv, slot++, 101, 18, 1, coalValidator));
		//mouth slots (left to right)
		addSlot(new RestrictedSlot(beInv, slot++, 38, 38, 1, coalValidator));
		addSlot(new RestrictedSlot(beInv, slot++, 59, 49, 1, coalValidator));
		addSlot(new RestrictedSlot(beInv, slot++, 80, 49, 1, coalValidator));
		addSlot(new RestrictedSlot(beInv, slot++, 101, 49, 1, coalValidator));
		addSlot(new RestrictedSlot(beInv, slot++, 122, 38, 1, coalValidator));
		//body slots (top to bottom)
		addSlot(new RestrictedSlot(beInv, slot++, 80, 74, 1, snowValidator));
		addSlot(new RestrictedSlot(beInv, slot++, 80, 103, 1, snowValidator));
		addSlot(new RestrictedSlot(beInv, slot++, 80, 132, 1, snowValidator));
		//weapon slot (always second last slot!)
		addSlot(new RestrictedSlot(beInv, slot++, 105, 89, 1, stack -> {
			for (ItemStack weapon : WEAPONS) {
				if (stack.getItem() == weapon.getItem())
					return true;
			}

			return false;
		}));
		//output (always last slot!)
		addSlot(new RestrictedSlot(beInv, slot++, 152, 132, 1, stack -> false));
	}

	@Override
	public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
		SnowmanBuilderInventory inv = be.getInventory();
		boolean clickedOutput = false;

		if (slotId == 36 + inv.getContainerSize() - 1 && !inv.getItem(inv.getContainerSize() - 1).isEmpty()) { //last slot
			clickedOutput = true;

			if (be.getProgress() == be.getMaxProgress()) {
				for (int i = 0; i < inv.getContainerSize() - 1; i++) { //remove all input items
					inv.getItemHandler().extractItem(i, 1, false);
				}
			}
		}

		if (!clickedOutput)
			super.clicked(slotId, dragType, clickType, player);
		else {
			if (be.getProgress() == be.getMaxProgress()) {
				be.resetProgress();

				if (player instanceof ServerPlayer sp && inv.getItem(inv.getContainerSize() - 1).getTag().getBoolean("evercold"))
					Snowmancy.CRAFT_EVERCOLD_SNOWMAN.trigger(sp);

				super.clicked(slotId, dragType, clickType, player);
			}
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack copy = ItemStack.EMPTY;
		Slot slot = slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();

			copy = slotStack.copy();

			if (index <= 35) {
				if (!moveItemStackTo(slotStack, 36, 36 + be.getInventory().getContainerSize(), false))
					return ItemStack.EMPTY;
			}
			else if (index >= 36) {
				if (!moveItemStackTo(slotStack, 0, 36, false))
					return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty())
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();
		}

		return copy;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
}
