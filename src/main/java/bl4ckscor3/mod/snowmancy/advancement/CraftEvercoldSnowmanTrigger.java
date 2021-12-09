package bl4ckscor3.mod.snowmancy.advancement;

import com.google.gson.JsonObject;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class CraftEvercoldSnowmanTrigger extends SimpleCriterionTrigger<CraftEvercoldSnowmanTrigger.Instance>
{
	public static final ResourceLocation ID = new ResourceLocation(Snowmancy.MODID, "craft_evercold_snowman");

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	public CraftEvercoldSnowmanTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite andPredicate, DeserializationContext conditionArrayParser)
	{
		return new CraftEvercoldSnowmanTrigger.Instance(andPredicate);
	}

	public void trigger(ServerPlayer player)
	{
		trigger(player, instance -> true);
	}

	public static class Instance extends AbstractCriterionTriggerInstance
	{
		public Instance(EntityPredicate.Composite andPredicate)
		{
			super(CraftEvercoldSnowmanTrigger.ID, andPredicate);
		}
	}
}
