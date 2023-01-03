package bl4ckscor3.mod.snowmancy.entity;

import net.minecraft.world.entity.ai.goal.RangedAttackGoal;

public class SnowmanAttackRangedGoal extends RangedAttackGoal {
	protected SnowmanCompanion snowman;

	public SnowmanAttackRangedGoal(SnowmanCompanion snowman) {
		super(snowman, 1.25D, 20, 10.0F);
		this.snowman = snowman;
	}

	@Override
	public boolean canUse() {
		return !AttackType.HIT.name().equals(snowman.getAttackType()) && super.canUse();
	}
}
