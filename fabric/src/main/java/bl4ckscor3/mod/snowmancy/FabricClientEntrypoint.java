package bl4ckscor3.mod.snowmancy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FabricClientEntrypoint implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SnowmancyClient.registerMenuScreen(new SnowmancyClient.MenuScreenRegistration() {
			@Override
			public <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> void register(MenuType<M> menuType, SnowmancyClient.ScreenConstructor<M, S> screenConstructor) {
				MenuScreens.register(menuType, screenConstructor::create);
			}
		});
		SnowmancyClient.registerRenderer(EntityRenderers::register);
		SnowmancyClient.registerLayerDefinitions((layerLocation, supplier) -> ModelLayerRegistry.registerModelLayer(layerLocation, supplier::get));
		SnowmancyClient.registerConditionalItemModelProperty(ConditionalItemModelProperties.ID_MAPPER::put);
	}
}
