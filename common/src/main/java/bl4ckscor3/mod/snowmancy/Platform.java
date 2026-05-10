package bl4ckscor3.mod.snowmancy;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface Platform {
	<R, T extends R> void register(ResourceKey<? extends Registry<R>> registry, Supplier<T> entry, String path);

	default <R, T extends R> void register(ResourceKey<? extends Registry<R>> registry, RegistryObject<T> registryObject) {
		register(registry, registryObject.object(), registryObject.id().getPath());
	}

	<T> void registerEntityDataSerializer(Supplier<EntityDataSerializer<T>> serializer, String path);

	<T extends BlockEntity> BlockEntityType<T> createBlockEntity(BlockEntityFactory<T> factory, Block validBlock);

	<T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuTypeFactory<T> factory);

	void openBuilderMenu(ServerPlayer player, MenuProvider be, BlockPos pos);

	@FunctionalInterface
	interface BlockEntityFactory<T extends BlockEntity> {
		T create(BlockPos pos, BlockState state);
	}

	@FunctionalInterface
	interface MenuTypeFactory<T extends AbstractContainerMenu> {
		T create(int containerId, Inventory inventory, BlockPos pos);
	}
}
