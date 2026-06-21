package bl4ckscor3.mod.snowmancy;

import java.util.Optional;
import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;

import bl4ckscor3.mod.snowmancy.lib.Platform;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricEntrypoint implements ModInitializer, Platform {
	@Override
	public void onInitialize() {
		Snowmancy.initialize(this);
		Snowmancy.registerDefaultAttributes(FabricDefaultAttributeRegistry::register);
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, _) -> Snowmancy.onLivingDeath(entity));
	}

	@Override
	public void openBuilderMenu(ServerPlayer player, MenuProvider be, BlockPos pos) {
		player.openMenu(new ExtendedMenuProvider<>() {
			@Override
			public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
				return be.createMenu(containerId, inventory, player);
			}

			@Override
			public Component getDisplayName() {
				return be.getDisplayName();
			}

			@Override
			public BlockPos getScreenOpeningData(ServerPlayer player) {
				return pos;
			}
		});
	}

	@Override
	public <T extends BlockEntity> BlockEntityType<T> createBlockEntity(BlockEntityFactory<T> factory, Block validBlock) {
		return FabricBlockEntityTypeBuilder.create(factory::create, validBlock).build();
	}

	@Override
	public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuTypeFactory<T> factory) {
		return new ExtendedMenuType<>(factory::create, BlockPos.STREAM_CODEC);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public <R, T extends R> void register(ResourceKey<? extends Registry<R>> registryKey, Supplier<T> entry, String path) {
		Optional<Holder.Reference<R>> registry = BuiltInRegistries.REGISTRY.get((ResourceKey) registryKey);

		if (registry.isEmpty()) {
			throw new IllegalArgumentException("Couldn't find registry " + registryKey);
		}

		Registry.register((Registry<R>) registry.get().value(), Snowmancy.id(path), entry.get());
	}

	@Override
	public <T> void registerEntityDataSerializer(Supplier<EntityDataSerializer<T>> serializer, String path) {
		FabricEntityDataRegistry.register(Snowmancy.id(path), serializer.get());
	}
}
