package bl4ckscor3.mod.snowmancy.gui;

import bl4ckscor3.mod.snowmancy.container.ContainerSnowmanBuilder;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiSnowmanBuilder extends GuiContainer
{
	public GuiSnowmanBuilder(InventoryPlayer playerInv, TileEntitySnowmanBuilder te)
	{
		super(new ContainerSnowmanBuilder(playerInv, te));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		drawDefaultBackground();
	}
}
