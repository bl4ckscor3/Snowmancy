package bl4ckscor3.mod.snowmancy;

import java.util.Arrays;

import bl4ckscor3.mod.snowmancy.advancement.CraftEvercoldSnowmanTrigger;
import bl4ckscor3.mod.snowmancy.block.BlockSnowmanBuilder;
import bl4ckscor3.mod.snowmancy.container.ContainerSnowmanBuilder;
import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import bl4ckscor3.mod.snowmancy.gui.GuiHandler;
import bl4ckscor3.mod.snowmancy.item.ItemFrozenSnowman;
import bl4ckscor3.mod.snowmancy.renderer.RenderSnowmanCompanion;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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

	@ObjectHolder(PREFIX + BlockSnowmanBuilder.NAME)
	public static final Block SNOWMAN_BUILDER = null;
	@ObjectHolder(PREFIX + "evercold_ice")
	public static final Block EVERCOLD_ICE = null;
	@ObjectHolder(PREFIX + ItemFrozenSnowman.NAME)
	public static final Item FROZEN_SNOWMAN = null;
	public static TileEntityType<TileEntitySnowmanBuilder> teTypeBuilder;
	public static EntityType<EntitySnowmanCompanion> eTypeSnowman;

	public static final CraftEvercoldSnowmanTrigger CRAFT_EVERCOLD_SNOWMAN = CriteriaTriggers.register(new CraftEvercoldSnowmanTrigger());
	public static final DamageSource SNOWMAN_DAMAGE = new DamageSource(PREFIX + "snowman_damage");
	public static final ItemGroup ITEM_GROUP = new ItemGroupSnowmancy();

	public Snowmancy()
	{
		Arrays.asList(Items.BOW,
				Items.DIAMOND_SWORD,
				Items.EGG,
				Items.GOLDEN_SWORD,
				Items.IRON_SWORD,
				Items.SNOWBALL,
				Items.STONE_SWORD,
				Items.WOODEN_SWORD).stream().forEach(ContainerSnowmanBuilder::registerWeapon);
		MinecraftForge.EVENT_BUS.addListener(this::onModelRegistry);
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> GuiHandler::getClientGuiElement);
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().register(new BlockSnowmanBuilder());
		event.getRegistry().register(new Block(Block.Properties.create(Material.ICE)
				.hardnessAndResistance(2.0F)
				.slipperiness(0.98F)
				.sound(SoundType.GLASS))
				.setRegistryName(PREFIX + "evercold_ice"));
	}

	@SubscribeEvent
	public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event)
	{
		teTypeBuilder = TileEntityType.register(SNOWMAN_BUILDER.getRegistryName().toString(), TileEntityType.Builder.create(TileEntitySnowmanBuilder::new));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(new ItemBlock(SNOWMAN_BUILDER, new Item.Properties().group(ITEM_GROUP)).setRegistryName(SNOWMAN_BUILDER.getRegistryName()));
		event.getRegistry().register(new ItemBlock(EVERCOLD_ICE, new Item.Properties().group(ITEM_GROUP)).setRegistryName(EVERCOLD_ICE.getRegistryName()));
		event.getRegistry().register(new ItemFrozenSnowman());
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event)
	{
		eTypeSnowman = EntityType.register(PREFIX + "snowman", EntityType.Builder.create(EntitySnowmanCompanion.class, EntitySnowmanCompanion::new).tracker(128, 1, true));
	}

	public void onModelRegistry(ModelRegistryEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySnowmanCompanion.class, manager -> new RenderSnowmanCompanion(manager));
	}
}
