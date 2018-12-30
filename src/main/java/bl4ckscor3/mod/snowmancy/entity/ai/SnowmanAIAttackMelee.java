package bl4ckscor3.mod.snowmancy.entity.ai;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.EnumHand;

public class SnowmanAIAttackMelee extends EntityAIAttackMelee
{
	public SnowmanAIAttackMelee(EntitySnowmanCompanion snowman)
	{
		super(snowman, 1.0F, true);
	}

	@Override
	public boolean shouldExecute()
	{
		return ((EntitySnowmanCompanion)attacker).getAttackType().equals(EnumAttackType.HIT.name()) && super.shouldExecute();
	}

	@Override
	protected void checkAndPerformAttack(EntityLivingBase enemy, double distToEnemySqr)
	{
		if(distToEnemySqr <= getAttackReachSqr(enemy) && attackTick <= 0)
		{
			attackTick = 20;
			attacker.swingArm(EnumHand.MAIN_HAND);
			enemy.attackEntityFrom(Snowmancy.SNOWMAN_DAMAGE, ((EntitySnowmanCompanion)attacker).getDamage());
		}
	}
}
