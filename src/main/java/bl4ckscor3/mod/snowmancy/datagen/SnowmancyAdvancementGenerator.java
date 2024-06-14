package bl4ckscor3.mod.snowmancy.datagen;

import java.util.Optional;
import java.util.function.Consumer;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider.AdvancementGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class SnowmancyAdvancementGenerator implements AdvancementGenerator {
	@Override
	public void generate(Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		//@formatter:off
		AdvancementHolder root = Advancement.Builder.advancement()
				.display(Snowmancy.SNOWMAN_BUILDER,
						Component.translatable("itemGroup.snowmancy"),
						Component.translatable("snowmancy.advancement.root.description"),
						ResourceLocation.fromNamespaceAndPath(Snowmancy.MODID, "textures/block/evercold_ice.png"),
						AdvancementType.TASK, true, true, false)
				.addCriterion("snowman_builder", RecipeCraftedTrigger.TriggerInstance.craftedItem(ResourceLocation.fromNamespaceAndPath(Snowmancy.MODID, "snowman_builder")))
				.save(saver, Snowmancy.MODID + ":root");
		AdvancementHolder snowmanCompanion = Advancement.Builder.advancement()
				.parent(root)
				.display(Snowmancy.FROZEN_SNOWMAN,
						Component.translatable("snowmancy.advancement.snowman_companion.title"),
						Component.translatable("snowmancy.advancement.snowman_companion.description"),
						null, AdvancementType.TASK, true, true, false)
				.addCriterion("frozen_snowman", InventoryChangeTrigger.TriggerInstance.hasItems(Snowmancy.FROZEN_SNOWMAN))
				.save(saver, Snowmancy.MODID + ":snowman_companion");
		Advancement.Builder.advancement()
				.parent(snowmanCompanion)
				.display(Snowmancy.EVERCOLD_ICE,
						Component.translatable("snowmancy.advancement.evercold_snowman.title"),
						Component.translatable("snowmancy.advancement.evercold_snowman.description"),
						null, AdvancementType.TASK, true, true, false)
				.addCriterion("evercold_snowman", Snowmancy.CRAFT_EVERCOLD_SNOWMAN.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty())))
				.save(saver, Snowmancy.MODID + ":evercold_snowman");
	}
}
