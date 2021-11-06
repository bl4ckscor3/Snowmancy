package bl4ckscor3.mod.snowmancy.block;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.tileentity.SnowmanBuilderTileEntity;
import net.minecraft.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class SnowmanBuilderBlock extends BaseEntityBlock
{
	public static final String NAME = "snowman_builder";

	public SnowmanBuilderBlock()
	{
		super(BlockBehaviour.Properties.of(Material.STONE).strength(3.5F).sound(SoundType.STONE));

		setRegistryName(Snowmancy.PREFIX + NAME);
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(!world.isClientSide)
		{
			BlockEntity te = world.getBlockEntity(pos);

			if(te instanceof MenuProvider)
				NetworkHooks.openGui((ServerPlayer)player, (MenuProvider)te, pos);

		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockEntity newBlockEntity(BlockGetter world)
	{
		return new SnowmanBuilderTileEntity();
	}
}
