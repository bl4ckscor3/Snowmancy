package bl4ckscor3.mod.snowmancy.block;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
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
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntitySnowmanBuilder();
	}
}
