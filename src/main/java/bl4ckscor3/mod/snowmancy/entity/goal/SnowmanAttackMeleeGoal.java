package bl4ckscor3.mod.snowmancy.entity.goal;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanionEntity;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class SnowmanAttackMeleeGoal extends MeleeAttackGoal
{
	public SnowmanAttackMeleeGoal(SnowmanCompanionEntity snowman)
	{
		super(snowman, 1.0F, true);
	}

	@Override
	public boolean canUse()
	{
		return ((SnowmanCompanionEntity)mob).getAttackType().equals(EnumAttackType.HIT.name()) && super.canUse();
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
	{
		if(distToEnemySqr <= getAttackReachSqr(enemy) && isTimeToAttack())
		{
			resetAttackCooldown();
			mob.swing(InteractionHand.MAIN_HAND);
			enemy.hurt(Snowmancy.SNOWMAN_DAMAGE, ((SnowmanCompanionEntity)mob).getDamage());
		}
	}
}
