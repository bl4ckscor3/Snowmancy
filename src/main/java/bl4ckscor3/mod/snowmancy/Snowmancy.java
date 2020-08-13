package bl4ckscor3.mod.snowmancy;

import java.util.Arrays;

import bl4ckscor3.mod.snowmancy.advancement.CraftEvercoldSnowmanTrigger;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderBlock;
import bl4ckscor3.mod.snowmancy.container.SnowmanBuilderContainer;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanionEntity;
import bl4ckscor3.mod.snowmancy.item.FrozenSnowmanItem;
import bl4ckscor3.mod.snowmancy.tileentity.SnowmanBuilderTileEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

@Mod(Snowmancy.MODID)
@EventBusSubscriber(bus=Bus.MOD)
public class Snowmancy
{
	public static final String MODID = "snowmancy";
	public static final String PREFIX = MODID + ":";

	@ObjectHolder(PREFIX + SnowmanBuilderBlock.NAME)
	public static final Block SNOWMAN_BUILDER = null;
	@ObjectHolder(PREFIX + "evercold_ice")
	public static final Block EVERCOLD_ICE = null;
	@ObjectHolder(PREFIX + FrozenSnowmanItem.NAME)
	public static final Item FROZEN_SNOWMAN = null;
	@ObjectHolder(PREFIX + SnowmanBuilderBlock.NAME)
	public static TileEntityType<SnowmanBuilderTileEntity> teTypeBuilder;
	@ObjectHolder(PREFIX + "snowman")
	public static EntityType<SnowmanCompanionEntity> eTypeSnowman;
	@ObjectHolder(PREFIX + SnowmanBuilderBlock.NAME)
	public static ContainerType<SnowmanBuilderContainer> cTypeSnowmanBuilder;

	public static final CraftEvercoldSnowmanTrigger CRAFT_EVERCOLD_SNOWMAN = CriteriaTriggers.register(new CraftEvercoldSnowmanTrigger());
	public static final DamageSource SNOWMAN_DAMAGE = new DamageSource(PREFIX + "snowman_damage");
	public static final ItemGroup ITEM_GROUP = new SnowmancyItemGroup();

	public Snowmancy()
	{
		Arrays.asList(Items.BOW,
				Items.DIAMOND_SWORD,
				Items.EGG,
				Items.GOLDEN_SWORD,
				Items.IRON_SWORD,
				Items.SNOWBALL,
				Items.STONE_SWORD,
				Items.WOODEN_SWORD).stream().forEach(SnowmanBuilderContainer::registerWeapon);
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().register(new SnowmanBuilderBlock());
		event.getRegistry().register(new Block(Block.Properties.create(Material.ICE)
				.hardnessAndResistance(2.0F)
				.slipperiness(0.98F)
				.sound(SoundType.GLASS))
				.setRegistryName(PREFIX + "evercold_ice"));
	}

	@SubscribeEvent
	public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event)
	{
		event.getRegistry().register(TileEntityType.Builder.create(SnowmanBuilderTileEntity::new, SNOWMAN_BUILDER).build(null).setRegistryName(SNOWMAN_BUILDER.getRegistryName()));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(new BlockItem(SNOWMAN_BUILDER, new Item.Properties().group(ITEM_GROUP)).setRegistryName(SNOWMAN_BUILDER.getRegistryName()));
		event.getRegistry().register(new BlockItem(EVERCOLD_ICE, new Item.Properties().group(ITEM_GROUP)).setRegistryName(EVERCOLD_ICE.getRegistryName()));
		event.getRegistry().register(new FrozenSnowmanItem());
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event)
	{
		EntityType<SnowmanCompanionEntity> snowmanCompanion = (EntityType<SnowmanCompanionEntity>)EntityType.Builder.<SnowmanCompanionEntity>create(SnowmanCompanionEntity::new, EntityClassification.CREATURE)
				.size(0.35F, 0.9F)
				.setTrackingRange(128)
				.setUpdateInterval(1)
				.setShouldReceiveVelocityUpdates(true)
				.build(PREFIX + "snowman")
				.setRegistryName(new ResourceLocation(MODID, "snowman"));

		event.getRegistry().register(snowmanCompanion);
		GlobalEntityTypeAttributes.put(snowmanCompanion, SnowmanCompanionEntity.getAttributes().create());
	}

	@SubscribeEvent
	public static void registerContainerTypes(RegistryEvent.Register<ContainerType<?>> event)
	{
		event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> new SnowmanBuilderContainer(windowId, inv.player.world, data.readBlockPos(), inv)).setRegistryName(SNOWMAN_BUILDER.getRegistryName()));
	}
}
