package bl4ckscor3.mod.snowmancy.entity.ai;

import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.entity.ai.goal.RangedAttackGoal;

public class SnowmanAIAttackRanged extends RangedAttackGoal
{
	protected EntitySnowmanCompanion snowman;

	public SnowmanAIAttackRanged(EntitySnowmanCompanion snowman)
	{
		super(snowman, 1.25D, 20, 10.0F);
		this.snowman = snowman;
	}

	@Override
	public boolean shouldExecute()
	{
		return EnumAttackType.valueOf(snowman.getAttackType()) != EnumAttackType.HIT && super.shouldExecute();
	}
}
