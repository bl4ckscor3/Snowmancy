package bl4ckscor3.mod.snowmancy.item;

import java.util.List;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemFrozenSnowman extends Item
{
	public static final String NAME = "frozen_snowman";

	public ItemFrozenSnowman()
	{
		super();

		setRegistryName(NAME);
		setTranslationKey(Snowmancy.PREFIX + NAME);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
		{
			Entity entity = new EntitySnowmanCompanion(world,
					player.getHeldItem(hand).getTagCompound().getBoolean("goldenCarrot"),
					player.getHeldItem(hand).getTagCompound().getString("attackType"),
					player.getHeldItem(hand).getTagCompound().getFloat("damage"));

			entity.setPosition(pos.getX() + 0.5F, pos.getY() + 1.0F, pos.getZ() + 0.5F);
			world.spawnEntity(entity);

			if(!player.isCreative())
				player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount() - 1);
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		tooltip.add(TextFormatting.GOLD + "Golden Carrot: " + TextFormatting.GRAY + stack.getTagCompound().getBoolean("goldenCarrot"));
		tooltip.add(TextFormatting.BLUE + "Attack Type: " + TextFormatting.GRAY + stack.getTagCompound().getString("attackType"));
		tooltip.add(TextFormatting.RED + "Damage: " + TextFormatting.GRAY + stack.getTagCompound().getFloat("damage"));
	}
}
