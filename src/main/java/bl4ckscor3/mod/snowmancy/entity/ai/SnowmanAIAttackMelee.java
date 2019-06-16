package bl4ckscor3.mod.snowmancy.entity.ai;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.util.Hand;

public class SnowmanAIAttackMelee extends MeleeAttackGoal
{
	public SnowmanAIAttackMelee(EntitySnowmanCompanion snowman)
	{
		super(snowman, 1.0F, true);
	}

	@Override
	public boolean shouldExecute()
	{
		return ((EntitySnowmanCompanion)field_75441_b).getAttackType().equals(EnumAttackType.HIT.name()) && super.shouldExecute();
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
	{
		if(distToEnemySqr <= getAttackReachSqr(enemy) && attackTick <= 0)
		{
			attackTick = 20;
			field_75441_b.swingArm(Hand.MAIN_HAND);
			enemy.attackEntityFrom(Snowmancy.SNOWMAN_DAMAGE, ((EntitySnowmanCompanion)field_75441_b).getDamage());
		}
	}
}
