package bl4ckscor3.mod.snowmancy.item;

import java.util.List;

import bl4ckscor3.mod.snowmancy.entity.AttackType;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanion;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class FrozenSnowmanItem extends Item {
	public FrozenSnowmanItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		Player player = context.getPlayer();
		ItemStack stack = player.getItemInHand(player.getUsedItemHand());
		BlockPos pos = context.getClickedPos();

		if (!level.isClientSide) {
			CompoundTag tag = stack.getTag();
			//@formatter:off
			Entity entity = new SnowmanCompanion(level,
					tag.getBoolean("goldenCarrot"),
					AttackType.fromTag(tag),
					tag.getFloat("damage"),
					tag.getBoolean("evercold"));
			//@formatter:on

			entity.setPos(pos.getX() + 0.5F, pos.getY() + 1.0F, pos.getZ() + 0.5F);
			level.addFreshEntity(entity);

			if (!player.isCreative())
				stack.shrink(1);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
		if (stack.hasTag()) {
			tooltip.add(new TextComponent(ChatFormatting.GOLD + "Golden Carrot: " + ChatFormatting.GRAY + stack.getTag().getBoolean("goldenCarrot")));
			tooltip.add(new TextComponent(ChatFormatting.BLUE + "Attack Type: " + ChatFormatting.GRAY + AttackType.fromTag(stack.getTag())));
			tooltip.add(new TextComponent(ChatFormatting.RED + "Damage: " + ChatFormatting.GRAY + stack.getTag().getFloat("damage")));
			tooltip.add(new TextComponent(ChatFormatting.AQUA + "Evercold: " + ChatFormatting.GRAY + stack.getTag().getBoolean("evercold")));
		}
	}
}
