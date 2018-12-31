package bl4ckscor3.mod.snowmancy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabSnowmancy extends CreativeTabs
{
	public CreativeTabSnowmancy()
	{
		super(getNextID(), Snowmancy.PREFIX + "creative_tab");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack createIcon()
	{
		return new ItemStack(Item.getItemFromBlock(Snowmancy.SNOWMAN_BUILDER));
	}
}
