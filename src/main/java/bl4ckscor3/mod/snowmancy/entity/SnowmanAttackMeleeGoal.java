package bl4ckscor3.mod.snowmancy.entity;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class SnowmanAttackMeleeGoal extends MeleeAttackGoal {
	public SnowmanAttackMeleeGoal(SnowmanCompanion snowman) {
		super(snowman, 1.0F, true);
	}

	@Override
	public boolean canUse() {
		return ((SnowmanCompanion) mob).getAttackType().equals(AttackType.HIT.name()) && super.canUse();
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
		if (distToEnemySqr <= getAttackReachSqr(enemy) && isTimeToAttack()) {
			resetAttackCooldown();
			mob.swing(InteractionHand.MAIN_HAND);
			enemy.hurt(Snowmancy.SNOWMAN_DAMAGE, ((SnowmanCompanion) mob).getDamage());
		}
	}
}
