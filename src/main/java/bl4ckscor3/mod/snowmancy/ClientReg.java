package bl4ckscor3.mod.snowmancy;

import bl4ckscor3.mod.snowmancy.renderer.SnowmanCompanionRenderer;
import bl4ckscor3.mod.snowmancy.screen.SnowmanBuilderScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus=Bus.MOD, modid=Snowmancy.MODID, value=Dist.CLIENT)
public class ClientReg
{
	@SubscribeEvent
	public static void onFMLClientSetup(FMLClientSetupEvent event)
	{
		MenuScreens.register(Snowmancy.cTypeSnowmanBuilder, SnowmanBuilderScreen::new);
		RenderingRegistry.registerEntityRenderingHandler(Snowmancy.eTypeSnowman, SnowmanCompanionRenderer::new);
	}
}
