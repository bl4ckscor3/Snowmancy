package bl4ckscor3.mod.snowmancy.datagen;

import java.util.concurrent.CompletableFuture;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

public class RecipeGenerator extends RecipeProvider {
	private final HolderGetter<Item> items;

	public RecipeGenerator(HolderLookup.Provider lookupProvider, RecipeOutput output) {
		super(lookupProvider, output);
		items = lookupProvider.lookupOrThrow(Registries.ITEM);
	}

	@Override
	protected final void buildRecipes() {
		//@formatter:off
		ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, Snowmancy.EVERCOLD_ICE)
		.pattern("PIP")
		.pattern("IPI")
		.pattern("PIP")
		.define('P', Items.PACKED_ICE)
		.define('I', Items.ICE)
		.unlockedBy("has_ice", has(Items.ICE))
		.save(output);
		ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, Snowmancy.SNOWMAN_BUILDER)
		.pattern("SNS")
		.pattern("CGC")
		.pattern("IPI")
		.define('S', Items.SNOW)
		.define('N', Items.ICE)
		.define('C', Tags.Items.COBBLESTONES)
		.define('G', Snowmancy.STAINED_GLASS_BLOCKS)
		.define('P', Items.PACKED_ICE)
		.define('I', Tags.Items.INGOTS_IRON)
		.unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
		.save(output);
		//@formatter:on
	}

	public static final class Runner extends RecipeProvider.Runner {
		public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(output, lookupProvider);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider lookupProvider, RecipeOutput output) {
			return new RecipeGenerator(lookupProvider, output);
		}

		@Override
		public String getName() {
			return "Snowmancy recipes";
		}
	}
}
