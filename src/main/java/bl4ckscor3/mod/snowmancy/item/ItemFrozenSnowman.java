package bl4ckscor3.mod.snowmancy.item;

import java.util.List;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemFrozenSnowman extends Item
{
	public static final String NAME = "frozen_snowman";

	public ItemFrozenSnowman()
	{
		super(new Item.Properties().group(Snowmancy.ITEM_GROUP));

		setRegistryName(NAME);
	}

	@Override
	public EnumActionResult onItemUse(ItemUseContext context)
	{
		World world = context.getWorld();
		EntityPlayer player = context.getPlayer();
		EnumHand hand = player.getActiveHand();
		BlockPos pos = context.getPos();

		if(!world.isRemote)
		{
			Entity entity = new EntitySnowmanCompanion(world,
					player.getHeldItem(hand).getTag().getBoolean("goldenCarrot"),
					player.getHeldItem(hand).getTag().getString("attackType"),
					player.getHeldItem(hand).getTag().getFloat("damage"),
					player.getHeldItem(hand).getTag().getBoolean("evercold"));

			entity.setPosition(pos.getX() + 0.5F, pos.getY() + 1.0F, pos.getZ() + 0.5F);
			world.spawnEntity(entity);

			if(!player.isCreative())
				player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount() - 1);
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if(stack.hasTag())
		{
			tooltip.add(new TextComponentString(TextFormatting.GOLD + "Golden Carrot: " + TextFormatting.GRAY + stack.getTag().getBoolean("goldenCarrot")));
			tooltip.add(new TextComponentString(TextFormatting.BLUE + "Attack Type: " + TextFormatting.GRAY + stack.getTag().getString("attackType")));
			tooltip.add(new TextComponentString(TextFormatting.RED + "Damage: " + TextFormatting.GRAY + stack.getTag().getFloat("damage")));
			tooltip.add(new TextComponentString(TextFormatting.AQUA + "Evercold: " + TextFormatting.GRAY + stack.getTag().getBoolean("evercold")));
		}
	}
}
