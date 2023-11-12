package bl4ckscor3.mod.snowmancy.block;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.AttackType;
import bl4ckscor3.mod.snowmancy.inventory.SnowmanBuilderInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.IItemHandler;

public class SnowmanBuilderBlockEntity extends BlockEntity implements MenuProvider {
	private SnowmanBuilderInventory inventory = new SnowmanBuilderInventory(this);
	private byte progress = 0;
	private final byte maxProgress = 8;
	private final LazyOptional<IItemHandler> inventoryHolder = LazyOptional.of(() -> inventory.getItemHandler());

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
				Item weapon = inventory.getItem(inventory.getContainerSize() - 2).getItem();
				CompoundTag tag = new CompoundTag();
				//@formatter:off
				AttackType attackType = (weapon == Items.BOW ? AttackType.ARROW :
					(weapon == Items.EGG ? AttackType.EGG :
						(weapon == Items.SNOWBALL ? AttackType.SNOWBALL : AttackType.HIT)));
				//@formatter:on

				tag.putBoolean("goldenCarrot", inventory.getItem(1).getItem() == Items.GOLDEN_CARROT);
				tag.putString("attackType", attackType.name());
				tag.putFloat("damage", attackType == AttackType.HIT && weapon instanceof SwordItem ? 4.0F + ((SwordItem) weapon).getDamage() : 0.0F);
				tag.putBoolean("evercold", inventory.getItem(0).getItem() == Snowmancy.EVERCOLD_ICE.get().asItem());
				stack.setTag(tag);
				inventory.getItemHandler().setStackInSlot(inventory.getContainerSize() - 1, stack);
			}
		}
	}

	/**
	 * Increases the progress of the current craft (if applicable) by one
	 */
	public void increaseProgress() {
		if (isCraftReady() && progress < maxProgress) {
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
	public void load(CompoundTag tag) {
		CompoundTag invTag = (CompoundTag) tag.get("SnowmanBuilderInventory");

		if (invTag != null) {
			for (int i = 0; i < inventory.getContents().size(); i++) {
				if (invTag.contains("Slot" + i))
					inventory.setItem(i, ItemStack.of((CompoundTag) invTag.get("Slot" + i)));
			}

			progress = tag.getByte("progress");
		}

		super.load(tag);
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		CompoundTag invTag = new CompoundTag();

		for (int i = 0; i < inventory.getContents().size(); i++) {
			invTag.put("Slot" + i, inventory.getItem(i).save(new CompoundTag()));
		}

		tag.put("SnowmanBuilderInventory", invTag);
		tag.putByte("progress", progress);
		super.saveAdditional(tag);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return saveWithoutMetadata();
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getTag());
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		load(tag);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == Capabilities.ITEM_HANDLER)
			return inventoryHolder.cast();
		else
			return super.getCapability(cap, side);
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

	/**
	 * Gets the crafting progress' maximum progress (upon which the craft will be completed)
	 *
	 * @return The crafting progress' maximum progress
	 */
	public byte getMaxProgress() {
		return maxProgress;
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
