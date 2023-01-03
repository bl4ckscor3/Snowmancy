package bl4ckscor3.mod.snowmancy.client;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus=Bus.MOD, modid=Snowmancy.MODID, value=Dist.CLIENT)
public class ClientReg
{
	public static final ModelLayerLocation SNOWMAN_LOCATION = new ModelLayerLocation(new ResourceLocation(Snowmancy.MODID, "snowman"), "main");

	@SubscribeEvent
	public static void onFMLClientSetup(FMLClientSetupEvent event)
	{
		event.enqueueWork(() -> MenuScreens.register(Snowmancy.SNOWMAN_BUILDER_MENU.get(), SnowmanBuilderScreen::new));
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(Snowmancy.SNOWMAN_ENTITY.get(), SnowmanCompanionRenderer::new);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(SNOWMAN_LOCATION, SnowmanCompanionModel::createLayer);
	}
}
