package bl4ckscor3.mod.snowmancy.entity.ai;

import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.entity.ai.EntityAIAttackMelee;

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
}
