package bl4ckscor3.mod.snowmancy.advancement;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

public class CraftEvercoldSnowmanTrigger implements ICriterionTrigger<CraftEvercoldSnowmanTrigger.Instance>
{
	public static final ResourceLocation ID = new ResourceLocation(Snowmancy.MODID, "craft_evercold_snowman");
	private final Map<PlayerAdvancements, CraftEvercoldSnowmanTrigger.Listeners> listenerMap = Maps.newHashMap();

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancements, Listener<CraftEvercoldSnowmanTrigger.Instance> listener)
	{
		CraftEvercoldSnowmanTrigger.Listeners listeners = listenerMap.computeIfAbsent(playerAdvancements, Listeners::new);
		listeners.add(listener);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancements, Listener<CraftEvercoldSnowmanTrigger.Instance> listener)
	{
		CraftEvercoldSnowmanTrigger.Listeners listeners = listenerMap.get(playerAdvancements);

		if(listeners != null)
		{
			listeners.remove(listener);

			if(listeners.isEmpty())
				listenerMap.remove(playerAdvancements);
		}
	}

	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancements)
	{
		listenerMap.remove(playerAdvancements);
	}

	@Override
	public CraftEvercoldSnowmanTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
	{
		return new CraftEvercoldSnowmanTrigger.Instance();
	}

	public void trigger(ServerPlayerEntity player)
	{
		CraftEvercoldSnowmanTrigger.Listeners listeners = listenerMap.get(player.getAdvancements());

		if(listeners != null)
			listeners.trigger();
	}

	public static class Instance extends CriterionInstance
	{
		public Instance()
		{
			super(CraftEvercoldSnowmanTrigger.ID);
		}
	}

	static class Listeners
	{
		private final PlayerAdvancements playerAdvancements;
		private final Set<Listener<CraftEvercoldSnowmanTrigger.Instance>> listeners = Sets.newHashSet();

		public Listeners(PlayerAdvancements playerAdvancements)
		{
			this.playerAdvancements = playerAdvancements;
		}

		public boolean isEmpty()
		{
			return listeners.isEmpty();
		}

		public void add(Listener<CraftEvercoldSnowmanTrigger.Instance> listener)
		{
			listeners.add(listener);
		}

		public void remove(Listener<CraftEvercoldSnowmanTrigger.Instance> listener)
		{
			listeners.remove(listener);
		}

		public void trigger()
		{
			for(Listener<CraftEvercoldSnowmanTrigger.Instance> listener : Lists.newArrayList(listeners))
			{
				listener.grantCriterion(playerAdvancements);
			}
		}
	}
}
