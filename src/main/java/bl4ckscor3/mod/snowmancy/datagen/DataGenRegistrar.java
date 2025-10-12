package bl4ckscor3.mod.snowmancy.datagen;

import java.util.List;
import java.util.Set;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Snowmancy.MODID)
public class DataGenRegistrar {
	private DataGenRegistrar() {}

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent.Client event) {
		event.createProvider((output, lookupProvider) -> new AdvancementProvider(output, lookupProvider, List.of(new SnowmancyAdvancementGenerator())));
		event.createBlockAndItemTags(BlockTagGenerator::new, ItemTagGenerator::new);
		event.createProvider((output, lookupProvider) -> new LootTableProvider(output, Set.of(), List.of(new SubProviderEntry(BlockLootTableGenerator::new, LootContextParamSets.BLOCK)), lookupProvider));
		event.createProvider(RecipeGenerator.Runner::new);
	}
}
