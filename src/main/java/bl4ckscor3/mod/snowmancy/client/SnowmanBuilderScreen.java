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

public class SnowmanBuilderScreen extends AbstractContainerScreen<SnowmanBuilderContainer>
{
	public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Snowmancy.MODID, "textures/gui/container/snowman_builder.png");
	private SnowmanBuilderBlockEntity te;

	public SnowmanBuilderScreen(SnowmanBuilderContainer container, Inventory playerInv, Component name)
	{
		super(container, playerInv, name);

		te = container.te;
		imageWidth = 176;
		imageHeight = 239;
	}

	@Override
	protected void renderLabels(PoseStack matrix, int mouseX, int mouseY)
	{
		int length = te.getProgress() * 2 + (te.isCraftReady() && te.getProgress() == 0 ? 1 : 0);
		int color = te.getProgress() < 5 ? 0xFFFF0000 : (te.getProgress() < 8 ? 0xFFFFFF00 : 0xFF00FF00); //red, yellow, green (0xAARRGGBB)

		if(!te.canOperate())
			drawString(matrix, font, "Biome too warm!", 0, -10, 0x00FFFF);

		fill(matrix, 152, 130, 152 + length, 131, color);
	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		super.render(matrix, mouseX, mouseY, partialTicks);

		renderTooltip(matrix, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		renderBackground(matrix);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem._setShaderTexture(0, GUI_TEXTURE);
		blit(matrix, (width - imageWidth) / 2, (height - imageHeight) / 2, 0, 0, imageWidth, imageHeight);
	}
}
