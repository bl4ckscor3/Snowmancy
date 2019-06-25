package bl4ckscor3.mod.snowmancy.block;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockSnowmanBuilder extends ContainerBlock
{
	public static final String NAME = "snowman_builder";

	public BlockSnowmanBuilder()
	{
		super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).sound(SoundType.STONE));

		setRegistryName(Snowmancy.PREFIX + NAME);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!world.isRemote)
		{
			TileEntity te = world.getTileEntity(pos);

			if(te instanceof INamedContainerProvider)
				NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)te, pos);

		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world)
	{
		return new TileEntitySnowmanBuilder();
	}
}
