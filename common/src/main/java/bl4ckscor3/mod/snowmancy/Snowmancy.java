package bl4ckscor3.mod.snowmancy;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderBlock;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderBlockEntity;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderContainer;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanion;
import bl4ckscor3.mod.snowmancy.item.FrozenSnowmanItem;
import bl4ckscor3.mod.snowmancy.item.SnowmanData;
import bl4ckscor3.mod.snowmancy.lib.Platform;
import bl4ckscor3.mod.snowmancy.lib.RegisteredBlock;
import bl4ckscor3.mod.snowmancy.lib.RegisteredItem;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class Snowmancy {
	public static final String MODID = "snowmancy";
	private static Platform platform;
	public static final RegisteredBlock<SnowmanBuilderBlock> SNOWMAN_BUILDER = RegisteredBlock.create("snowman_builder", SnowmanBuilderBlock::new, () -> BlockBehaviour.Properties.of().strength(3.5F).sound(SoundType.STONE).requiresCorrectToolForDrops());
	public static final RegisteredBlock<Block> EVERCOLD_ICE = RegisteredBlock.create("evercold_ice", Block::new, () -> BlockBehaviour.Properties.of()
		.strength(2.0F)
		.friction(0.98F)
		.sound(SoundType.GLASS));
	public static final RegisteredItem<BlockItem> SNOWMAN_BUILDER_ITEM = RegisteredItem.blockItem(SNOWMAN_BUILDER, Item.Properties::new);
	public static final RegisteredItem<BlockItem> EVERCOLD_ICE_ITEM = RegisteredItem.blockItem(EVERCOLD_ICE, Item.Properties::new);
	public static final RegisteredItem<FrozenSnowmanItem> FROZEN_SNOWMAN = RegisteredItem.item("frozen_snowman", FrozenSnowmanItem::new, Item.Properties::new);
	public static final Supplier<BlockEntityType<SnowmanBuilderBlockEntity>> SNOWMAN_BUILDER_BLOCK_ENTITY = Suppliers.memoize(() -> platform.createBlockEntity(SnowmanBuilderBlockEntity::new, SNOWMAN_BUILDER.get()));
	public static final Supplier<MenuType<SnowmanBuilderContainer>> SNOWMAN_BUILDER_MENU = Suppliers.memoize(() -> platform.createMenuType(SnowmanBuilderContainer::new));
	public static final Supplier<EntityType<SnowmanCompanion>> SNOWMAN_ENTITY = Suppliers.memoize(() -> EntityType.Builder.<SnowmanCompanion>of(SnowmanCompanion::new, MobCategory.CREATURE)
		.sized(0.35F, 0.9F)
		.clientTrackingRange(128)
		.updateInterval(1)
		.build(ResourceKey.create(Registries.ENTITY_TYPE, id("snowman"))));
	public static final Supplier<EntityDataSerializer<SnowmanData>> SNOWMAN_DATA_SERIALIZER = Suppliers.memoize(() -> EntityDataSerializer.forValueType(SnowmanData.STREAM_CODEC));
	public static final Supplier<PlayerTrigger> CRAFT_EVERCOLD_SNOWMAN = Suppliers.memoize(PlayerTrigger::new);
	public static final ResourceKey<DamageType> SNOWMAN_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, id("snowman_damage"));
	public static final Supplier<CreativeModeTab> CREATIVE_TAB = Suppliers.memoize(() -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
		.icon(() -> new ItemStack(SNOWMAN_BUILDER.get()))
		.title(Component.translatable("itemGroup.snowmancy"))
		.displayItems((_, output) -> {
			output.acceptAll(List.of(
				new ItemStack(SNOWMAN_BUILDER.get()),
				new ItemStack(FROZEN_SNOWMAN.get()),
				new ItemStack(EVERCOLD_ICE.get())));
		}).build());
	public static final Supplier<DataComponentType<SnowmanData>> SNOWMAN_DATA = Suppliers.memoize(() -> DataComponentType.<SnowmanData>builder().persistent(SnowmanData.CODEC).networkSynchronized(SnowmanData.STREAM_CODEC).cacheEncoding().build());
	public static final TagKey<Item> STAINED_GLASS_BLOCKS = TagKey.create(Registries.ITEM, id("stained_glass"));
	public static final TagKey<Item> CAN_BE_USED_AS_WEAPON = TagKey.create(Registries.ITEM, id("can_be_used_as_weapon"));

	public synchronized static void initialize(Platform platform) {
		if (Snowmancy.platform != null) {
			throw new IllegalArgumentException(MODID + " platform has already been initialized");
		}

		Snowmancy.platform = platform;
		platform.register(Registries.BLOCK, SNOWMAN_BUILDER);
		platform.register(Registries.BLOCK, EVERCOLD_ICE);
		platform.register(Registries.ITEM, SNOWMAN_BUILDER_ITEM);
		platform.register(Registries.ITEM, EVERCOLD_ICE_ITEM);
		platform.register(Registries.ITEM, FROZEN_SNOWMAN);
		platform.register(Registries.BLOCK_ENTITY_TYPE, SNOWMAN_BUILDER_BLOCK_ENTITY, "snowman_builder");
		platform.register(Registries.MENU, SNOWMAN_BUILDER_MENU, "snowman_builder");
		platform.register(Registries.ENTITY_TYPE, SNOWMAN_ENTITY, "snowman");
		platform.registerEntityDataSerializer(SNOWMAN_DATA_SERIALIZER, "snowman_data");
		platform.register(Registries.TRIGGER_TYPE, CRAFT_EVERCOLD_SNOWMAN, "craft_evercold_snowman");
		platform.register(Registries.CREATIVE_MODE_TAB, CREATIVE_TAB, "tab");
		platform.register(Registries.DATA_COMPONENT_TYPE, SNOWMAN_DATA, "snowman_data");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MODID, path);
	}

	public static Platform platform() {
		return platform;
	}

	public static void registerDefaultAttributes(EntityAttributeRegistration registrar) {
		registrar.register(SNOWMAN_ENTITY.get(), SnowmanCompanion.createAttributes().build());
	}

	@FunctionalInterface
	public interface EntityAttributeRegistration {
		<T extends LivingEntity> void register(EntityType<T> type, AttributeSupplier attributeSupplier);
	}

	public static void onProjectileImpactThrowable(Projectile projectile, HitResult hitResult) {
		if (projectile instanceof Snowball snowball && hitResult.getType() == HitResult.Type.BLOCK) {
			BlockEntity be = snowball.level().getBlockEntity(((BlockHitResult) hitResult).getBlockPos());

			if (be instanceof SnowmanBuilderBlockEntity builder) {
				if (builder.isCraftReady() && builder.getProgress() < SnowmanBuilderBlockEntity.MAX_PROGRESS)
					be.getLevel().playSound(null, be.getBlockPos(), SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 1.0F, 1.0F);

				builder.increaseProgress();
			}
		}
	}

	public static void onLivingDeath(Entity entity) {
		if (entity instanceof SnowmanCompanion snowman)
			Block.popResource(snowman.level(), snowman.blockPosition(), snowman.createItem());
	}
}
