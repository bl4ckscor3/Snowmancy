package bl4ckscor3.mod.snowmancy.entity.goal;

import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanionEntity;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.entity.ai.goal.RangedAttackGoal;

public class SnowmanAttackRangedGoal extends RangedAttackGoal
{
	protected SnowmanCompanionEntity snowman;

	public SnowmanAttackRangedGoal(SnowmanCompanionEntity snowman)
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
