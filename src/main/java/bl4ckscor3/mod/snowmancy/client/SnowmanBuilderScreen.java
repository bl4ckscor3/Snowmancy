package bl4ckscor3.mod.snowmancy.client;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderBlockEntity;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class SnowmanBuilderScreen extends AbstractContainerScreen<SnowmanBuilderContainer> {
	public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Snowmancy.MODID, "textures/gui/container/snowman_builder.png");
	private final Component biomeTooWarm = Component.translatable("snowmancy.screen.biomeTooWarm");
	private SnowmanBuilderBlockEntity be;

	public SnowmanBuilderScreen(SnowmanBuilderContainer container, Inventory playerInv, Component name) {
		super(container, playerInv, name, DEFAULT_IMAGE_WIDTH, 239);

		be = container.be;
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		int length = be.getProgress() * 2 + (be.isCraftReady() && be.getProgress() == 0 ? 1 : 0);
		int color = be.getProgress() < 5 ? 0xFFFF0000 : (be.getProgress() < 8 ? 0xFFFFFF00 : 0xFF00FF00); //red, yellow, green (0xAARRGGBB)

		if (!be.canOperate())
			guiGraphics.text(minecraft.font, biomeTooWarm, 0, -10, 0xFF00FFFF);

		guiGraphics.fill(152, 130, 152 + length, 131, color);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		extractTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, (width - imageWidth) / 2, (height - imageHeight) / 2, 0, 0, imageWidth, imageHeight, 256, 256);
	}
}
