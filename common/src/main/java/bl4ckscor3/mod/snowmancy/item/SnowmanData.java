package bl4ckscor3.mod.snowmancy.item;

import java.util.function.Consumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import bl4ckscor3.mod.snowmancy.entity.AttackType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record SnowmanData(AttackType attackType, float damage, boolean evercold, boolean goldenCarrot) implements TooltipProvider {

	public static final SnowmanData EMPTY = new SnowmanData(AttackType.NONE, 0.0F, false, false);
	//@formatter:off
	public static final Codec<SnowmanData> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					StringRepresentable.fromValues(AttackType::values).fieldOf("attack_type").forGetter(SnowmanData::attackType),
					Codec.FLOAT.fieldOf("damage").forGetter(SnowmanData::damage),
					Codec.BOOL.fieldOf("evercold").forGetter(SnowmanData::evercold),
					Codec.BOOL.fieldOf("golden_carrot").forGetter(SnowmanData::goldenCarrot))
			.apply(instance, SnowmanData::new));
	public static final StreamCodec<FriendlyByteBuf, SnowmanData> STREAM_CODEC = StreamCodec.composite(
			enumCodec(AttackType.class), SnowmanData::attackType,
			ByteBufCodecs.FLOAT, SnowmanData::damage,
			ByteBufCodecs.BOOL, SnowmanData::evercold,
			ByteBufCodecs.BOOL, SnowmanData::goldenCarrot,
			SnowmanData::new);
	//@formatter:on
	public static SnowmanData random(RandomSource random) {
		AttackType attackType = AttackType.values()[random.nextInt(AttackType.values().length)];

		return new SnowmanData(attackType, attackType.isMelee() ? random.nextInt(21) + random.nextFloat() : 0.0F, random.nextBoolean(), random.nextBoolean());
	}

	@Override
	public void addToTooltip(TooltipContext ctx, Consumer<Component> lineAdder, TooltipFlag flag, DataComponentGetter getter) {
		lineAdder.accept(Component.translatable("snowmancy.tooltip.goldenCarrot", Component.translatable("snowmancy.tooltip." + goldenCarrot).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.GOLD));
		lineAdder.accept(Component.translatable("snowmancy.tooltip.attackType", Component.translatable(attackType.getDescriptionId()).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.BLUE));
		lineAdder.accept(Component.translatable("snowmancy.tooltip.damage", Component.literal("" + damage).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.RED));
		lineAdder.accept(Component.translatable("snowmancy.tooltip.evercold", Component.translatable("snowmancy.tooltip." + evercold).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.AQUA));
	}

	public static <B extends FriendlyByteBuf, V extends Enum<V>> StreamCodec<B, V> enumCodec(Class<V> enumClass) {
		return new StreamCodec<>() {
			@Override
			public V decode(B buf) {
				return buf.readEnum(enumClass);
			}

			@Override
			public void encode(B buf, V value) {
				buf.writeEnum(value);
			}
		};
	}
}
