package bl4ckscor3.mod.snowmancy.item;

import java.util.function.Consumer;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
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
			SnowmanData snowmanData = stack.get(Snowmancy.SNOWMAN_DATA);

			if (snowmanData == null)
				snowmanData = SnowmanData.random(level.getRandom());

			Entity entity = new SnowmanCompanion(level, snowmanData);

			entity.setPos(pos.getX() + 0.5F, pos.getY() + 1.0F, pos.getZ() + 0.5F);
			level.addFreshEntity(entity);

			if (!player.isCreative())
				stack.shrink(1);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		SnowmanData snowmanData = stack.get(Snowmancy.SNOWMAN_DATA);

		if (snowmanData != null && (!stack.has(DataComponents.TOOLTIP_DISPLAY) || stack.get(DataComponents.TOOLTIP_DISPLAY).shows(Snowmancy.SNOWMAN_DATA.get())))
			snowmanData.addToTooltip(ctx, tooltipAdder, flag, stack);
	}
}
