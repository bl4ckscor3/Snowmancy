package bl4ckscor3.mod.snowmancy.client;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Snowmancy.MODID, value = Dist.CLIENT)
public class ClientReg {
	public static final ModelLayerLocation SNOWMAN_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Snowmancy.MODID, "snowman"), "main");

	private ClientReg() {}

	@SubscribeEvent
	public static void onFMLClientSetup(RegisterMenuScreensEvent event) {
		event.register(Snowmancy.SNOWMAN_BUILDER_MENU.get(), SnowmanBuilderScreen::new);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(Snowmancy.SNOWMAN_ENTITY.get(), SnowmanCompanionRenderer::new);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(SNOWMAN_LOCATION, SnowmanCompanionModel::createLayer);
	}

	@SubscribeEvent
	public static void registerConditionalItemModelProperty(RegisterConditionalItemModelPropertyEvent event) {
		event.register(Identifier.fromNamespaceAndPath(Snowmancy.MODID, "golden_nose"), GoldenNose.MAP_CODEC);
	}
}
