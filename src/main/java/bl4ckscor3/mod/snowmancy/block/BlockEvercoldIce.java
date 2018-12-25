package bl4ckscor3.mod.snowmancy.block;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockEvercoldIce extends Block
{
	public static final String NAME = "evercold_ice";

	public BlockEvercoldIce()
	{
		super(Material.ICE);

		setRegistryName(NAME);
		setTranslationKey(Snowmancy.PREFIX + NAME);
		setHardness(2.0F);
		setSoundType(SoundType.GLASS);
	}

	@Override
	public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return 0.98F;
	}
}
