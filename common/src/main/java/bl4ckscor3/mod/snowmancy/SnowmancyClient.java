package bl4ckscor3.mod.snowmancy;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;

import bl4ckscor3.mod.snowmancy.client.GoldenNose;
import bl4ckscor3.mod.snowmancy.client.SnowmanBuilderScreen;
import bl4ckscor3.mod.snowmancy.client.SnowmanCompanionModel;
import bl4ckscor3.mod.snowmancy.client.SnowmanCompanionRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class SnowmancyClient {
	public static final ModelLayerLocation SNOWMAN_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Snowmancy.MODID, "snowman"), "main");

	private SnowmancyClient() {}

	public static void registerMenuScreen(MenuScreenRegistration event) {
		event.register(Snowmancy.SNOWMAN_BUILDER_MENU.get(), SnowmanBuilderScreen::new);
	}

	public static void registerRenderer(EntityRendererRegistration registrar) {
		registrar.register(Snowmancy.SNOWMAN_ENTITY.get(), SnowmanCompanionRenderer::new);
	}

	public static void registerLayerDefinitions(LayerDefinitionRegistration registrar) {
		registrar.register(SNOWMAN_LOCATION, SnowmanCompanionModel::createLayer);
	}

	public static void registerConditionalItemModelProperty(ConditionalItemModelPropertyRegistration registrar) {
		registrar.register(Identifier.fromNamespaceAndPath(Snowmancy.MODID, "golden_nose"), GoldenNose.MAP_CODEC);
	}

	@FunctionalInterface
	public interface EntityRendererRegistration {
		<T extends Entity> void register(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider);
	}

	@FunctionalInterface
	public interface LayerDefinitionRegistration {
		void register(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier);
	}

	@FunctionalInterface
	public interface MenuScreenRegistration {
		<M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> void register(MenuType<M> menuType, ScreenConstructor<M, S> screenConstructor);
	}

	@FunctionalInterface
	public interface ScreenConstructor<M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> {
		S create(M menu, Inventory inv, Component title);
	}

	@FunctionalInterface
	public interface ConditionalItemModelPropertyRegistration {
		<T extends ConditionalItemModelProperty> void register(Identifier id, MapCodec<T> codec);
	}
}
