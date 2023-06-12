package bl4ckscor3.mod.snowmancy;

import java.util.Arrays;
import java.util.List;

import bl4ckscor3.mod.snowmancy.advancement.CraftEvercoldSnowmanTrigger;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderBlock;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderBlockEntity;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderContainer;
import bl4ckscor3.mod.snowmancy.entity.AttackType;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanion;
import bl4ckscor3.mod.snowmancy.item.FrozenSnowmanItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;

@Mod(Snowmancy.MODID)
@EventBusSubscriber(bus = Bus.MOD)
public class Snowmancy {
	public static final String MODID = "snowmancy";
	public static final String PREFIX = MODID + ":";
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
	public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(Keys.ENTITY_DATA_SERIALIZERS, MODID);
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
	public static final RegistryObject<SnowmanBuilderBlock> SNOWMAN_BUILDER = BLOCKS.register("snowman_builder", () -> new SnowmanBuilderBlock(Properties.of().strength(3.5F).sound(SoundType.STONE)));
	//@formatter:off
	public static final RegistryObject<Block> EVERCOLD_ICE = BLOCKS.register("evercold_ice", () -> new Block(Properties.of()
			.strength(2.0F)
			.friction(0.98F)
			.sound(SoundType.GLASS)));
	//@formatter:on
	public static final RegistryObject<BlockItem> SNOWMAN_BUILDER_ITEM = ITEMS.register("snowman_builder", () -> new BlockItem(SNOWMAN_BUILDER.get(), new Item.Properties()));
	public static final RegistryObject<BlockItem> EVERCOLD_ITEM = ITEMS.register("evercold_ice", () -> new BlockItem(EVERCOLD_ICE.get(), new Item.Properties()));
	public static final RegistryObject<FrozenSnowmanItem> FROZEN_SNOWMAN = ITEMS.register("frozen_snowman", () -> new FrozenSnowmanItem(new Item.Properties()));
	public static final RegistryObject<BlockEntityType<SnowmanBuilderBlockEntity>> SNOWMAN_BUILDER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("snowman_builder", () -> BlockEntityType.Builder.of(SnowmanBuilderBlockEntity::new, SNOWMAN_BUILDER.get()).build(null));
	public static final RegistryObject<MenuType<SnowmanBuilderContainer>> SNOWMAN_BUILDER_MENU = MENU_TYPES.register("snowman_builder", () -> IForgeMenuType.create((windowId, inv, data) -> new SnowmanBuilderContainer(windowId, inv.player.level(), data.readBlockPos(), inv)));
	//@formatter:off
	public static final RegistryObject<EntityType<SnowmanCompanion>> SNOWMAN_ENTITY = ENTITY_TYPES.register("snowman", () -> EntityType.Builder.<SnowmanCompanion>of(SnowmanCompanion::new, MobCategory.CREATURE)
			.sized(0.35F, 0.9F)
			.setTrackingRange(128)
			.setUpdateInterval(1)
			.setShouldReceiveVelocityUpdates(true)
			.build(PREFIX + "snowman"));
	//@formatter:on
	public static final RegistryObject<EntityDataSerializer<AttackType>> ATTACK_TYPE_SERIALIZER = ENTITY_DATA_SERIALIZERS.register("attack_type", () -> EntityDataSerializer.simpleEnum(AttackType.class));
	public static final CraftEvercoldSnowmanTrigger CRAFT_EVERCOLD_SNOWMAN = CriteriaTriggers.register(new CraftEvercoldSnowmanTrigger());
	public static final ResourceKey<DamageType> SNOWMAN_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(MODID, "snowman_damage"));
	//@formatter:off
	public static final RegistryObject<CreativeModeTab> TECHNICAL_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.icon(() -> new ItemStack(SNOWMAN_BUILDER.get()))
			.title(Component.translatable("itemGroup.snowmancy"))
			.displayItems((displayParameters, output) -> {
				output.acceptAll(List.of(
						new ItemStack(SNOWMAN_BUILDER.get()),
						new ItemStack(FROZEN_SNOWMAN.get()),
						new ItemStack(EVERCOLD_ICE.get())));
			}).build());
	//@formatter:on

	public Snowmancy() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
		BLOCK_ENTITY_TYPES.register(modEventBus);
		ENTITY_TYPES.register(modEventBus);
		ENTITY_DATA_SERIALIZERS.register(modEventBus);
		MENU_TYPES.register(modEventBus);
		CREATIVE_MODE_TABS.register(modEventBus);
		//@formatter:off
		Arrays.asList(Items.BOW,
				Items.DIAMOND_SWORD,
				Items.EGG,
				Items.GOLDEN_SWORD,
				Items.IRON_SWORD,
				Items.NETHERITE_SWORD,
				Items.SNOWBALL,
				Items.STONE_SWORD,
				Items.WOODEN_SWORD).stream().forEach(SnowmanBuilderContainer::registerWeapon);
		//@formatter:on
	}

	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(SNOWMAN_ENTITY.get(), SnowmanCompanion.createAttributes().build());
	}
}
