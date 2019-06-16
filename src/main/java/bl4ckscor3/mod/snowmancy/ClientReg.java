package bl4ckscor3.mod.snowmancy;

import bl4ckscor3.mod.snowmancy.gui.GuiSnowmanBuilder;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus=Bus.MOD, modid=Snowmancy.MODID, value=Dist.CLIENT)
public class ClientReg
{
	@SubscribeEvent
	public static void onFMLClientSetup(FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(Snowmancy.cTypeSnowmanBuilder, GuiSnowmanBuilder::new);
	}
}
