package bl4ckscor3.mod.snowmancy.datagen;

import java.util.concurrent.CompletableFuture;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

public class RecipeGenerator extends RecipeProvider {
	public RecipeGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider);
	}

	@Override
	protected final void buildRecipes(RecipeOutput recipeOutput) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Snowmancy.EVERCOLD_ICE)
		.pattern("PIP")
		.pattern("IPI")
		.pattern("PIP")
		.define('P', Items.PACKED_ICE)
		.define('I', Items.ICE)
		.unlockedBy("has_ice", has(Items.ICE))
		.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Snowmancy.SNOWMAN_BUILDER)
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
		.save(recipeOutput);
		//@formatter:on
	}
}
