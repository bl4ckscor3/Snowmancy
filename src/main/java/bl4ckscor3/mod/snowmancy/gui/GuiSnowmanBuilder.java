package bl4ckscor3.mod.snowmancy.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.block.BlockSnowmanBuilder;
import bl4ckscor3.mod.snowmancy.container.ContainerSnowmanBuilder;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiSnowmanBuilder extends ContainerScreen<ContainerSnowmanBuilder>
{
	public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Snowmancy.MODID, "textures/gui/container/" + BlockSnowmanBuilder.NAME + ".png");
	private TileEntitySnowmanBuilder te;

	public GuiSnowmanBuilder(ContainerSnowmanBuilder container, PlayerInventory playerInv, ITextComponent name)
	{
		super(container, playerInv, name);

		te = container.te;
		xSize = 176;
		ySize = 239;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		int length = te.getProgress() * 2 + (te.isCraftReady() && te.getProgress() == 0 ? 1 : 0);
		int color = te.getProgress() < 5 ? 0xFFFF0000 : (te.getProgress() < 8 ? 0xFFFFFF00 : 0xFF00FF00); //red, yellow, green (0xAARRGGBB)

		if(!te.canOperate())
			drawString(font, "Biome too warm!", 0, -10, 0x00FFFF);

		fill(152, 130, 152 + length, 131, color);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		super.render(mouseX, mouseY, partialTicks);

		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		renderBackground();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
		blit((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
	}
}
