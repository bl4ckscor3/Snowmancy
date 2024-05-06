package bl4ckscor3.mod.snowmancy.block;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.AttackType;
import bl4ckscor3.mod.snowmancy.inventory.SnowmanBuilderInventory;
import bl4ckscor3.mod.snowmancy.item.SnowmanData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemAttributeModifiers.Entry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SnowmanBuilderBlockEntity extends BlockEntity implements MenuProvider {
	public static final byte MAX_PROGRESS = 8;
	private SnowmanBuilderInventory inventory = new SnowmanBuilderInventory(this);
	private byte progress = 0;

	public SnowmanBuilderBlockEntity(BlockPos pos, BlockState state) {
		super(Snowmancy.SNOWMAN_BUILDER_BLOCK_ENTITY.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, SnowmanBuilderBlockEntity be) {
		be.tick();
	}

	public void tick() {
		if (canOperate()) {
			for (int i = 0; i < inventory.getContainerSize() - 1; i++) { //last slot is output
				if (inventory.getItem(i).isEmpty()) {
					inventory.getItemHandler().setStackInSlot(inventory.getContainerSize() - 1, ItemStack.EMPTY);
					resetProgress();
					return;
				}
			}

			if (!isCraftReady()) {
				ItemStack stack = new ItemStack(Snowmancy.FROZEN_SNOWMAN.get());
				ItemStack weapon = inventory.getItem(inventory.getContainerSize() - 2);
				AttackType attackType = AttackType.byItem(weapon);

				//@formatter:off
				stack.set(Snowmancy.SNOWMAN_DATA, new SnowmanData(
						attackType,
						attackType == AttackType.HIT && weapon.getItem() instanceof SwordItem ? 4.0F + getAttackDamage(weapon) : 0.0F,
						inventory.getItem(0).is(Snowmancy.EVERCOLD_ICE_ITEM),
						inventory.getItem(1).is(Items.GOLDEN_CARROT)));
				//@formatter:on
				inventory.getItemHandler().setStackInSlot(inventory.getContainerSize() - 1, stack);
			}
		}
	}

	private float getAttackDamage(ItemStack stack) {
		ItemAttributeModifiers attributeModifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

		for (Entry entry : attributeModifiers.modifiers()) {
			if (entry.attribute().is(Attributes.ATTACK_DAMAGE))
				return (float) entry.modifier().amount();
		}

		return 0.0F;
	}

	/**
	 * Increases the progress of the current craft (if applicable) by one
	 */
	public void increaseProgress() {
		if (isCraftReady() && progress < MAX_PROGRESS) {
			progress++;
			markDirtyClient();
		}
	}

	/**
	 * Resets the progress of the ongoing craft, probably because it got aborted or was finished
	 */
	public void resetProgress() {
		progress = 0;
		markDirtyClient();
	}

	/**
	 * Checks if the crafting status is ready by checking if the last slot (output slot) of this block entity's inventory is not
	 * empty
	 *
	 * @return true if the crafting status is ready, false otherwhise
	 */
	public boolean isCraftReady() {
		return !inventory.getItem(inventory.getContainerSize() - 1).isEmpty();
	}

	/**
	 * @return true if the machine can work in the current climate, false otherwhise
	 */
	public boolean canOperate() {
		int cooling = 0;
		float temperature = getLevel().getBiome(worldPosition).value().getBaseTemperature();
		boolean cold = temperature < 0.2F;
		boolean medium = temperature < 1.0F;
		boolean warm = temperature >= 1.0F;

		for (Direction facing : Direction.values()) {
			if (getLevel().getBlockState(worldPosition.relative(facing)).getBlock() == Snowmancy.EVERCOLD_ICE.get())
				cooling++;
		}

		return switch (cooling) {
			case 0, 1 -> cold;
			case 2, 3 -> cold || medium;
			default -> cold || medium || warm;
		};
	}

	/**
	 * Used server-side whenever the block entity changes in a way that requires the client to know
	 */
	public void markDirtyClient() {
		setChanged();

		if (level != null)
			level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		CompoundTag invTag = (CompoundTag) tag.get("SnowmanBuilderInventory");

		if (invTag != null) {
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				if (invTag.contains("Slot" + i)) {
					CompoundTag stackTag = invTag.getCompound("Slot" + i);
					ItemStack stack = ItemStack.EMPTY;

					if (stackTag.getInt("count") > 0)
						stack = ItemStack.parse(lookupProvider, stackTag).orElse(ItemStack.EMPTY);

					inventory.setItem(i, stack);
				}
			}
		}

		progress = tag.getByte("progress");
		super.loadAdditional(tag, lookupProvider);
	}

	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		CompoundTag invTag = new CompoundTag();

		for (int i = 0; i < inventory.getContents().size(); i++) {
			ItemStack stack = inventory.getItem(i);

			if (!stack.isEmpty())
				invTag.put("Slot" + i, stack.save(lookupProvider, new CompoundTag()));
		}

		tag.put("SnowmanBuilderInventory", invTag);
		tag.putByte("progress", progress);
		super.saveAdditional(tag, lookupProvider);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		return saveCustomOnly(lookupProvider);
	}

	/**
	 * @return This block entity's inventory
	 */
	public SnowmanBuilderInventory getInventory() {
		return inventory;
	}

	/**
	 * Gets the crafting progress
	 *
	 * @return The crafting progress
	 */
	public byte getProgress() {
		return progress;
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
		return new SnowmanBuilderContainer(windowId, level, worldPosition, inv);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable(Snowmancy.SNOWMAN_BUILDER.get().getDescriptionId());
	}
}
