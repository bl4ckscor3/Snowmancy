package bl4ckscor3.mod.snowmancy;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

@Mod(Snowmancy.MODID)
@EventBusSubscriber
public class NeoEntrypoint implements Platform {
	private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registers = new HashMap<>();
	private final IEventBus modBus;

	public NeoEntrypoint(IEventBus modBus) {
		this.modBus = modBus;
		Snowmancy.initialize(this);
	}

	@Override
	public void openBuilderMenu(ServerPlayer player, MenuProvider be, BlockPos pos) {
		player.openMenu(be, pos);
	}

	@Override
	public <T extends BlockEntity> BlockEntityType<T> createBlockEntity(BlockEntityFactory<T> factory, Block validBlock) {
		return new BlockEntityType<>(factory::create, validBlock);
	}

	@Override
	public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuTypeFactory<T> factory) {
		return IMenuTypeExtension.create((windowId, inv, data) -> factory.create(windowId, inv, data.readBlockPos()));
	}

	@Override
	public <R, T extends R> void register(ResourceKey<? extends Registry<R>> registry, Supplier<T> entry, String path) {
		@SuppressWarnings("unchecked")
		DeferredRegister<R> register = (DeferredRegister<R>) registers.computeIfAbsent(
			registry,
			_ -> {
				DeferredRegister<R> r = DeferredRegister.create(registry, Snowmancy.MODID);

				r.register(modBus);
				return r;
			}
		);
		register.register(path, entry);
	}

	@Override
	public <T> void registerEntityDataSerializer(Supplier<EntityDataSerializer<T>> serializer, String path) {
		register(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, serializer, path);
	}

	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.Item.BLOCK, Snowmancy.SNOWMAN_BUILDER_BLOCK_ENTITY.get(), (be, _) -> VanillaContainerWrapper.of(be));
	}

	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		Snowmancy.registerDefaultAttributes(event::put);
	}

	@SubscribeEvent
	public static void onProjectileImpactThrowable(ProjectileImpactEvent event) {
		Snowmancy.onProjectileImpactThrowable(event.getProjectile(), event.getRayTraceResult());
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		Snowmancy.onLivingDeath(event.getEntity());
	}
}
