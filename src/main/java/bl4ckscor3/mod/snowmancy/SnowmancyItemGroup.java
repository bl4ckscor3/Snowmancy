package bl4ckscor3.mod.snowmancy;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SnowmancyItemGroup extends CreativeModeTab
{
	public SnowmancyItemGroup()
	{
		super(TABS.length, Snowmancy.PREFIX + "item_group");
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack makeIcon()
	{
		return new ItemStack(Snowmancy.SNOWMAN_BUILDER.get().asItem());
	}
}
