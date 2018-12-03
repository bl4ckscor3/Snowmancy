package bl4ckscor3.mod.snowmancy.block;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.gui.GuiHandler;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSnowmanBuilder extends BlockContainer
{
	public static final String NAME = "snowman_builder";

	public BlockSnowmanBuilder()
	{
		super(Material.ROCK);

		setTranslationKey(Snowmancy.PREFIX + NAME);
		setRegistryName(NAME);
		setHardness(3.5F);
		setSoundType(SoundType.STONE);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
			player.openGui(Snowmancy.MODID, GuiHandler.BUILDER_GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntitySnowmanBuilder();
	}
}
