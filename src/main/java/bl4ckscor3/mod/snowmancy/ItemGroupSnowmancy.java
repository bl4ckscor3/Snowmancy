package bl4ckscor3.mod.snowmancy;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemGroupSnowmancy extends ItemGroup
{
	public ItemGroupSnowmancy()
	{
		super(GROUPS.length, Snowmancy.PREFIX + "item_group");
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack createIcon()
	{
		return new ItemStack(Snowmancy.SNOWMAN_BUILDER.asItem());
	}
}
