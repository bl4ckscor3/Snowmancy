package bl4ckscor3.mod.snowmancy.entity.ai;

import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.entity.ai.EntityAIAttackRanged;

public class SnowmanAIAttackRanged extends EntityAIAttackRanged
{
	private EntitySnowmanCompanion snowman;

	public SnowmanAIAttackRanged(EntitySnowmanCompanion snowman)
	{
		super(snowman, 1.25D, 20, 10.0F);
		this.snowman = snowman;
	}

	@Override
	public boolean shouldExecute()
	{
		EnumAttackType type = EnumAttackType.valueOf(snowman.getAttackType());

		return type != EnumAttackType.ARROW && type != EnumAttackType.HIT && super.shouldExecute();
	}
}
