package bl4ckscor3.mod.snowmancy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;

import bl4ckscor3.mod.snowmancy.entity.AttackType;
import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;

/**
 * Makes sure the snowman companion's data is converted to components
 */
@Mixin(ItemStackComponentizationFix.class)
public class ItemStackComponentizationFixMixin {
	@Inject(method = "fixItemStack", at = @At("TAIL"))
	private static void snowmancy$fixItemStacks(ItemStackComponentizationFix.ItemStackData itemStackData, Dynamic<?> dynamic, CallbackInfo ci) {
		if (itemStackData.is("snowmancy:frozen_snowman")) {
			OptionalDynamic<?> attackTypeTag = itemStackData.removeTag("attackType");
			DataResult<String> legacyAttackType = attackTypeTag.asString();
			String attackType = null;

			if (!legacyAttackType.isError()) {
				for (AttackType type : AttackType.values()) {
					if (type.name().equals(legacyAttackType.getOrThrow())) {
						attackType = type.getSerializedName();
						break;
					}
				}
			}

			if (attackType == null) {
				int attackTypeInData = attackTypeTag.asInt(0);

				if (attackTypeInData >= 0 && attackTypeInData < AttackType.values().length)
					attackType = AttackType.values()[attackTypeInData].getSerializedName();
				else
					attackType = AttackType.NONE.getSerializedName();
			}

			//@formatter:off
			itemStackData.setComponent("snowmancy:snowman_data", dynamic.emptyMap()
					.set("attack_type", dynamic.createString(attackType))
					.set("damage", dynamic.createFloat(itemStackData.removeTag("damage").asFloat(0.0F)))
					.set("evercold", dynamic.createBoolean(itemStackData.removeTag("evercold").asBoolean(false)))
					.set("golden_carrot", dynamic.createBoolean(itemStackData.removeTag("goldenCarrot").asBoolean(false))));
			//@formatter:on
		}
	}
}
