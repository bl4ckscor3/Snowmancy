package bl4ckscor3.mod.snowmancy.gui;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.block.BlockSnowmanBuilder;
import bl4ckscor3.mod.snowmancy.container.ContainerSnowmanBuilder;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSnowmanBuilder extends GuiContainer
{
	public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Snowmancy.MODID, "textures/gui/container/" + BlockSnowmanBuilder.NAME + ".png");

	public GuiSnowmanBuilder(InventoryPlayer playerInv, TileEntitySnowmanBuilder te)
	{
		super(new ContainerSnowmanBuilder(playerInv, te));

		xSize = 176;
		ySize = 239;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);

		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(new ResourceLocation(Snowmancy.MODID, "textures/gui/container/" + BlockSnowmanBuilder.NAME + ".png"));
		drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
	}
}