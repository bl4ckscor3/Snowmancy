package bl4ckscor3.mod.snowmancy.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderBlockEntity;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderContainer;
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
	protected void renderLabels(PoseStack matrix, int mouseX, int mouseY) {
		int length = be.getProgress() * 2 + (be.isCraftReady() && be.getProgress() == 0 ? 1 : 0);
		int color = be.getProgress() < 5 ? 0xFFFF0000 : (be.getProgress() < 8 ? 0xFFFFFF00 : 0xFF00FF00); //red, yellow, green (0xAARRGGBB)

		if (!be.canOperate())
			drawString(matrix, font, biomeTooWarm, 0, -10, 0x00FFFF);

		fill(matrix, 152, 130, 152 + length, 131, color);
	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
		super.render(matrix, mouseX, mouseY, partialTicks);

		renderTooltip(matrix, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
		renderBackground(matrix);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem._setShaderTexture(0, TEXTURE);
		blit(matrix, (width - imageWidth) / 2, (height - imageHeight) / 2, 0, 0, imageWidth, imageHeight);
	}
}
