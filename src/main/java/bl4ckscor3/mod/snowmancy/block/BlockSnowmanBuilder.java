package bl4ckscor3.mod.snowmancy.block;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.gui.GuiHandler;
import bl4ckscor3.mod.snowmancy.gui.SnowmanBuilderInteractionObject;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockSnowmanBuilder extends BlockContainer
{
	public static final String NAME = "snowman_builder";

	public BlockSnowmanBuilder()
	{
		super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).sound(SoundType.STONE));

		setRegistryName(Snowmancy.PREFIX + NAME);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote && player instanceof EntityPlayerMP)
			NetworkHooks.openGui((EntityPlayerMP)player, new SnowmanBuilderInteractionObject(GuiHandler.BUILDER_GUI_ID, world, pos), data -> data.writeBlockPos(pos));
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world)
	{
		return new TileEntitySnowmanBuilder();
	}
}
