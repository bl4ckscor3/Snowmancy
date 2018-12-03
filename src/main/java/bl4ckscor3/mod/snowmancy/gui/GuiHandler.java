package bl4ckscor3.mod.snowmancy.gui;

import bl4ckscor3.mod.snowmancy.container.ContainerSnowmanBuilder;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	public static final int BUILDER_GUI_ID = 1;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

		if(id == BUILDER_GUI_ID && te instanceof TileEntitySnowmanBuilder)
			return new ContainerSnowmanBuilder(player.inventory, (TileEntitySnowmanBuilder)te);
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

		if(id == BUILDER_GUI_ID && te instanceof TileEntitySnowmanBuilder)
			return new GuiSnowmanBuilder(player.inventory, (TileEntitySnowmanBuilder)te);
		return null;
	}
}
