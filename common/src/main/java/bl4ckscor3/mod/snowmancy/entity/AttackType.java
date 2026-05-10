package bl4ckscor3.mod.snowmancy.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public enum AttackType implements StringRepresentable {
	NONE,
	ARROW,
	EGG,
	HIT,
	SNOWBALL;

	public boolean isRanged() {
		return this == ARROW || this == EGG || this == SNOWBALL;
	}

	public boolean isMelee() {
		return this == HIT;
	}

	public String getDescriptionId() {
		return "snowmancy.attackType." + name().toLowerCase();
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase();
	}

	public static AttackType byItem(ItemStack stack) {
		if (stack.isEmpty())
			return NONE;
		else if (stack.is(Items.BOW))
			return ARROW;
		else if (stack.is(Items.EGG))
			return EGG;
		else if (stack.is(Items.SNOWBALL))
			return SNOWBALL;
		else
			return HIT;
	}

	public static AttackType fromTag(CompoundTag tag) {
		//legacy data support
		return tag.getString("attackType").map(legacyAttackType -> {
			if (!legacyAttackType.isEmpty()) {
				for (AttackType type : AttackType.values()) {
					if (type.name().equals(legacyAttackType)) {
						return type;
					}
				}
			}

			return AttackType.NONE;
		}).orElseGet(() -> {
			int attackTypeData = tag.getIntOr("attackType", 0);

			if (attackTypeData >= 0 && attackTypeData < AttackType.values().length)
				return AttackType.values()[attackTypeData];

			return AttackType.NONE;
		});
	}
}
