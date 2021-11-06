package bl4ckscor3.mod.snowmancy.block;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.tileentity.SnowmanBuilderTileEntity;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraft.block.AbstractBlock;

public class SnowmanBuilderBlock extends ContainerBlock
{
	public static final String NAME = "snowman_builder";

	public SnowmanBuilderBlock()
	{
		super(AbstractBlock.Properties.of(Material.STONE).strength(3.5F).sound(SoundType.STONE));

		setRegistryName(Snowmancy.PREFIX + NAME);
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!world.isClientSide)
		{
			TileEntity te = world.getBlockEntity(pos);

			if(te instanceof INamedContainerProvider)
				NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)te, pos);

		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader world)
	{
		return new SnowmanBuilderTileEntity();
	}
}
