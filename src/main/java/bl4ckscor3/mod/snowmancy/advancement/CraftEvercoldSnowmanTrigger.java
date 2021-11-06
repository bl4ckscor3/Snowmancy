package bl4ckscor3.mod.snowmancy.advancement;

import com.google.gson.JsonObject;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class CraftEvercoldSnowmanTrigger extends AbstractCriterionTrigger<CraftEvercoldSnowmanTrigger.Instance>
{
	public static final ResourceLocation ID = new ResourceLocation(Snowmancy.MODID, "craft_evercold_snowman");

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	public CraftEvercoldSnowmanTrigger.Instance createInstance(JsonObject json, EntityPredicate.AndPredicate andPredicate, ConditionArrayParser conditionArrayParser)
	{
		return new CraftEvercoldSnowmanTrigger.Instance(andPredicate);
	}

	public void trigger(ServerPlayerEntity player)
	{
		trigger(player, instance -> true);
	}

	public static class Instance extends CriterionInstance
	{
		public Instance(EntityPredicate.AndPredicate andPredicate)
		{
			super(CraftEvercoldSnowmanTrigger.ID, andPredicate);
		}
	}
}
