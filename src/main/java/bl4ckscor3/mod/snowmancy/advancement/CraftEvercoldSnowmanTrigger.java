package bl4ckscor3.mod.snowmancy.advancement;

import java.util.Optional;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

public class CraftEvercoldSnowmanTrigger extends SimpleCriterionTrigger<CraftEvercoldSnowmanTrigger.Instance> {
	@Override
	public CraftEvercoldSnowmanTrigger.Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext ctx) {
		return new CraftEvercoldSnowmanTrigger.Instance(predicate);
	}

	public void trigger(ServerPlayer player) {
		trigger(player, instance -> true);
	}

	public static class Instance extends AbstractCriterionTriggerInstance {
		public Instance(Optional<ContextAwarePredicate> predicate) {
			super(predicate);
		}
	}
}
