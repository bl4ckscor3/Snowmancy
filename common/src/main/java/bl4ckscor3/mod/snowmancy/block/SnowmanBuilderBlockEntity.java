package bl4ckscor3.mod.snowmancy.block;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.AttackType;
import bl4ckscor3.mod.snowmancy.item.SnowmanData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemAttributeModifiers.Entry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SnowmanBuilderBlockEntity extends BaseContainerBlockEntity {
	public static final int SLOTS = 14;
	private NonNullList<ItemStack> contents = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
	public static final byte MAX_PROGRESS = 8;
	private byte progress = 0;

	public SnowmanBuilderBlockEntity(BlockPos pos, BlockState state) {
		super(Snowmancy.SNOWMAN_BUILDER_BLOCK_ENTITY.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, SnowmanBuilderBlockEntity be) {
		be.tick();
	}

	public void tick() {
		if (canOperate()) {
			for (int i = 0; i < getContainerSize() - 1; i++) { //last slot is output
				if (getItem(i).isEmpty()) {
					setItem(getContainerSize() - 1, ItemStack.EMPTY);
					resetProgress();
					return;
				}
			}

			if (!isCraftReady()) {
				ItemStack stack = new ItemStack(Snowmancy.FROZEN_SNOWMAN.get());
				ItemStack weapon = getItem(getContainerSize() - 2);
				AttackType attackType = AttackType.byItem(weapon);

				//@formatter:off
				stack.set(Snowmancy.SNOWMAN_DATA.get(), new SnowmanData(
						attackType,
						attackType == AttackType.HIT ? getAttackDamage(weapon) : 0.0F,
						getItem(0).is(Snowmancy.EVERCOLD_ICE_ITEM.get()),
						getItem(1).is(Items.GOLDEN_CARROT)));
				//@formatter:on
				setItem(getContainerSize() - 1, stack);
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
		return !getItem(getContainerSize() - 1).isEmpty();
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
	public void loadAdditional(ValueInput tag) {
		ValueInput invTag = tag.child("SnowmanBuilderInventory").orElse(null);

		if (invTag != null) {
			for (int i = 0; i < getContainerSize(); i++) {
				setItem(i, invTag.read("Slot" + i, ItemStack.CODEC).orElse(ItemStack.EMPTY));
			}
		}

		progress = tag.getByteOr("progress", (byte) 0);
		super.loadAdditional(tag);
	}

	@Override
	public void saveAdditional(ValueOutput tag) {
		ValueOutput child = tag.child("SnowmanBuilderInventory");

		for (int i = 0; i < getItems().size(); i++) {
			ItemStack stack = getItem(i);

			if (!stack.isEmpty())
				child.store("Slot" + i, ItemStack.CODEC, stack);
		}

		tag.putByte("progress", progress);
		super.saveAdditional(tag);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		return saveCustomOnly(lookupProvider);
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return contents;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		contents = items;
	}

	@Override
	public int getContainerSize() {
		return SLOTS;
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
	public AbstractContainerMenu createMenu(int windowId, Inventory inv) {
		return new SnowmanBuilderContainer(windowId, inv, worldPosition);
	}

	@Override
	public Component getDefaultName() {
		return Component.translatable(Snowmancy.SNOWMAN_BUILDER.get().getDescriptionId());
	}
}
