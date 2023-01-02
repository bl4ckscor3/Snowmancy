package bl4ckscor3.mod.snowmancy.item;

import java.util.List;

import bl4ckscor3.mod.snowmancy.entity.AttackType;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanion;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
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
			CompoundTag tag = stack.getOrCreateTag();
			RandomSource random = level.getRandom();
			boolean goldenCarrot = getOrRandomBoolean(tag, "goldenCarrot", random);
			AttackType attackType;
			float damage;
			boolean evercold = getOrRandomBoolean(tag, "evercold", random);

			if (!tag.contains("attackType"))
				attackType = AttackType.values()[random.nextInt(AttackType.values().length)];
			else
				attackType = AttackType.fromTag(tag);

			if (!tag.contains("damage"))
				damage = attackType.isMelee() ? random.nextInt(21) + random.nextFloat() : 0.0F;
			else
				damage = tag.getFloat("damage");

			Entity entity = new SnowmanCompanion(level, goldenCarrot, attackType, damage, evercold);

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
			CompoundTag tag = stack.getTag();
			boolean goldenCarrot = tag.getBoolean("goldenCarrot");
			AttackType attackType = AttackType.fromTag(tag);
			float damage = tag.getFloat("damage");
			boolean evercold = tag.getBoolean("evercold");

			tooltip.add(Component.translatable("snowmancy.tooltip.goldenCarrot", Component.translatable("snowmancy.tooltip." + goldenCarrot).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.GOLD));
			tooltip.add(Component.translatable("snowmancy.tooltip.attackType", Component.translatable(attackType.getDescriptionId()).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.BLUE));
			tooltip.add(Component.translatable("snowmancy.tooltip.damage", Component.literal("" + damage).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.RED));
			tooltip.add(Component.translatable("snowmancy.tooltip.evercold", Component.translatable("snowmancy.tooltip." + evercold).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.AQUA));
		}
	}

	private boolean getOrRandomBoolean(CompoundTag tag, String key, RandomSource random) {
		if (!tag.contains(key))
			return random.nextBoolean();

		return tag.getBoolean(key);
	}
}
