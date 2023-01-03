package bl4ckscor3.mod.snowmancy.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public enum AttackType {
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

	public static AttackType fromTag(CompoundTag tag) {
		AttackType attackType = NONE;

		//legacy data support
		if (tag.contains("attackType", Tag.TAG_STRING)) {
			String legacyAttackType = tag.getString("attackType");

			if (!legacyAttackType.isEmpty()) {
				for (AttackType type : AttackType.values()) {
					if (type.name().equals(legacyAttackType)) {
						attackType = type;
						break;
					}
				}
			}
		}
		else {
			int attackTypeData = tag.getInt("attackType");

			if (attackTypeData >= 0 && attackTypeData < AttackType.values().length)
				attackType = AttackType.values()[attackTypeData];
		}

		return attackType;
	}
}
