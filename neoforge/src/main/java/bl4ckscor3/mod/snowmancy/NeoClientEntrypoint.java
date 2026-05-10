package bl4ckscor3.mod.snowmancy;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(value = Snowmancy.MODID, dist = Dist.CLIENT)
@EventBusSubscriber
public class NeoClientEntrypoint {
	@SubscribeEvent
	public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
		SnowmancyClient.registerMenuScreen(new SnowmancyClient.MenuScreenRegistration() {
			@Override
			public <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> void register(MenuType<M> menuType, SnowmancyClient.ScreenConstructor<M, S> screenConstructor) {
				event.register(menuType, screenConstructor::create);
			}
		});
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		SnowmancyClient.registerRenderer(event::registerEntityRenderer);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		SnowmancyClient.registerLayerDefinitions(event::registerLayerDefinition);
	}

	@SubscribeEvent
	public static void registerConditionalItemModelProperty(RegisterConditionalItemModelPropertyEvent event) {
		SnowmancyClient.registerConditionalItemModelProperty(event::register);
	}
}
