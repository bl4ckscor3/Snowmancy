package bl4ckscor3.mod.snowmancy.item;

import java.util.List;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanion;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class FrozenSnowmanItem extends Item
{
	public static final String NAME = "frozen_snowman";

	public FrozenSnowmanItem()
	{
		super(new Item.Properties().tab(Snowmancy.ITEM_GROUP));

		setRegistryName(NAME);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Level world = context.getLevel();
		Player player = context.getPlayer();
		InteractionHand hand = player.getUsedItemHand();
		BlockPos pos = context.getClickedPos();

		if(!world.isClientSide)
		{
			Entity entity = new SnowmanCompanion(world,
					player.getItemInHand(hand).getTag().getBoolean("goldenCarrot"),
					player.getItemInHand(hand).getTag().getString("attackType"),
					player.getItemInHand(hand).getTag().getFloat("damage"),
					player.getItemInHand(hand).getTag().getBoolean("evercold"));

			entity.setPos(pos.getX() + 0.5F, pos.getY() + 1.0F, pos.getZ() + 0.5F);
			world.addFreshEntity(entity);

			if(!player.isCreative())
				player.getItemInHand(hand).setCount(player.getItemInHand(hand).getCount() - 1);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		if(stack.hasTag())
		{
			tooltip.add(new TextComponent(ChatFormatting.GOLD + "Golden Carrot: " + ChatFormatting.GRAY + stack.getTag().getBoolean("goldenCarrot")));
			tooltip.add(new TextComponent(ChatFormatting.BLUE + "Attack Type: " + ChatFormatting.GRAY + stack.getTag().getString("attackType")));
			tooltip.add(new TextComponent(ChatFormatting.RED + "Damage: " + ChatFormatting.GRAY + stack.getTag().getFloat("damage")));
			tooltip.add(new TextComponent(ChatFormatting.AQUA + "Evercold: " + ChatFormatting.GRAY + stack.getTag().getBoolean("evercold")));
		}
	}
}
