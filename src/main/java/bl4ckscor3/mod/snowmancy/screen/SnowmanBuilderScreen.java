package bl4ckscor3.mod.snowmancy.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderBlock;
import bl4ckscor3.mod.snowmancy.container.SnowmanBuilderContainer;
import bl4ckscor3.mod.snowmancy.tileentity.SnowmanBuilderTileEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SnowmanBuilderScreen extends ContainerScreen<SnowmanBuilderContainer>
{
	public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Snowmancy.MODID, "textures/gui/container/" + SnowmanBuilderBlock.NAME + ".png");
	private SnowmanBuilderTileEntity te;

	public SnowmanBuilderScreen(SnowmanBuilderContainer container, PlayerInventory playerInv, ITextComponent name)
	{
		super(container, playerInv, name);

		te = container.te;
		xSize = 176;
		ySize = 239;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrix, int mouseX, int mouseY)
	{
		int length = te.getProgress() * 2 + (te.isCraftReady() && te.getProgress() == 0 ? 1 : 0);
		int color = te.getProgress() < 5 ? 0xFFFF0000 : (te.getProgress() < 8 ? 0xFFFFFF00 : 0xFF00FF00); //red, yellow, green (0xAARRGGBB)

		if(!te.canOperate())
			drawString(matrix, font, "Biome too warm!", 0, -10, 0x00FFFF);

		fill(matrix, 152, 130, 152 + length, 131, color);
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks)
	{
		super.render(matrix, mouseX, mouseY, partialTicks);

		func_230459_a_(matrix, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		renderBackground(matrix);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
		blit(matrix, (width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
	}
}
