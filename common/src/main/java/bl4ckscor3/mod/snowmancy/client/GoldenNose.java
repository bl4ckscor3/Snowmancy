package bl4ckscor3.mod.snowmancy.client;

import com.mojang.serialization.MapCodec;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.item.SnowmanData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public record GoldenNose() implements ConditionalItemModelProperty {
	public static final MapCodec<GoldenNose> MAP_CODEC = MapCodec.unit(new GoldenNose());

	@Override
	public boolean get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext displayContext) {
		return stack.getOrDefault(Snowmancy.SNOWMAN_DATA.get(), SnowmanData.EMPTY).goldenCarrot();
	}

	@Override
	public MapCodec<? extends ConditionalItemModelProperty> type() {
		return MAP_CODEC;
	}
}
