package bl4ckscor3.mod.snowmancy.gui;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.FMLPlayMessages.OpenContainer;

public class GuiHandler
{
	public static final ResourceLocation BUILDER_GUI_ID = new ResourceLocation(Snowmancy.MODID, "builder");

	public static GuiScreen getClientGuiElement(OpenContainer message)
	{
		EntityPlayerSP player = Minecraft.getInstance().player;

		if(message.getId().equals(BUILDER_GUI_ID))
		{
			TileEntity te = Minecraft.getInstance().world.getTileEntity(message.getAdditionalData().readBlockPos());

			if(te instanceof TileEntitySnowmanBuilder)
				return new GuiSnowmanBuilder(player.inventory, (TileEntitySnowmanBuilder)te);
		}

		return null;
	}
}
