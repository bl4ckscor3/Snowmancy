package bl4ckscor3.mod.snowmancy.entity.goal;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanionEntity;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.util.Hand;

public class SnowmanAttackMeleeGoal extends MeleeAttackGoal
{
	public SnowmanAttackMeleeGoal(SnowmanCompanionEntity snowman)
	{
		super(snowman, 1.0F, true);
	}

	@Override
	public boolean shouldExecute()
	{
		return ((SnowmanCompanionEntity)attacker).getAttackType().equals(EnumAttackType.HIT.name()) && super.shouldExecute();
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
	{
		if(distToEnemySqr <= getAttackReachSqr(enemy) && func_234041_j_() <= 0) //getAttackTick
		{
			func_234039_g_(); //resetAttackTick
			attacker.swingArm(Hand.MAIN_HAND);
			enemy.attackEntityFrom(Snowmancy.SNOWMAN_DAMAGE, ((SnowmanCompanionEntity)attacker).getDamage());
		}
	}
}
