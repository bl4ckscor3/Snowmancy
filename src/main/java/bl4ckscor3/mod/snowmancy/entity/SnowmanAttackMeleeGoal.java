package bl4ckscor3.mod.snowmancy.entity;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class SnowmanAttackMeleeGoal extends MeleeAttackGoal {
	public SnowmanAttackMeleeGoal(SnowmanCompanion snowman) {
		super(snowman, 1.0F, true);
	}

	@Override
	public boolean canUse() {
		return ((SnowmanCompanion) mob).getAttackType().isMelee() && super.canUse();
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity enemy) {
		if (canPerformAttack(enemy)) {
			resetAttackCooldown();
			mob.swing(InteractionHand.MAIN_HAND);
			enemy.hurt(new DamageSource(enemy.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(Snowmancy.SNOWMAN_DAMAGE), mob), ((SnowmanCompanion) mob).getDamage());
		}
	}
}
