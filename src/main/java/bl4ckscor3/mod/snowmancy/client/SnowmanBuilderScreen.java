package bl4ckscor3.mod.snowmancy.client;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderBlockEntity;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SnowmanBuilderScreen extends AbstractContainerScreen<SnowmanBuilderContainer> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(Snowmancy.MODID, "textures/gui/container/snowman_builder.png");
	private final Component biomeTooWarm = Component.translatable("snowmancy.screen.biomeTooWarm");
	private SnowmanBuilderBlockEntity be;

	public SnowmanBuilderScreen(SnowmanBuilderContainer container, Inventory playerInv, Component name) {
		super(container, playerInv, name);

		be = container.be;
		imageHeight = 239;
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		int length = be.getProgress() * 2 + (be.isCraftReady() && be.getProgress() == 0 ? 1 : 0);
		int color = be.getProgress() < 5 ? 0xFFFF0000 : (be.getProgress() < 8 ? 0xFFFFFF00 : 0xFF00FF00); //red, yellow, green (0xAARRGGBB)

		if (!be.canOperate())
			guiGraphics.drawString(minecraft.font, biomeTooWarm, 0, -10, 0x00FFFF);

		guiGraphics.fill(152, 130, 152 + length, 131, color);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		guiGraphics.blit(TEXTURE, (width - imageWidth) / 2, (height - imageHeight) / 2, 0, 0, imageWidth, imageHeight);
	}
}
