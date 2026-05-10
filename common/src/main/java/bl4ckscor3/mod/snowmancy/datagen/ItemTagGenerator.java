package bl4ckscor3.mod.snowmancy.datagen;

import java.util.concurrent.CompletableFuture;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

public class ItemTagGenerator extends VanillaItemTagsProvider {
	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(Snowmancy.STAINED_GLASS_BLOCKS).add(
			Items.WHITE_STAINED_GLASS,
			Items.ORANGE_STAINED_GLASS,
			Items.MAGENTA_STAINED_GLASS,
			Items.LIGHT_BLUE_STAINED_GLASS,
			Items.YELLOW_STAINED_GLASS,
			Items.LIME_STAINED_GLASS,
			Items.PINK_STAINED_GLASS,
			Items.GRAY_STAINED_GLASS,
			Items.LIGHT_GRAY_STAINED_GLASS,
			Items.CYAN_STAINED_GLASS,
			Items.PURPLE_STAINED_GLASS,
			Items.BLUE_STAINED_GLASS,
			Items.BROWN_STAINED_GLASS,
			Items.GREEN_STAINED_GLASS,
			Items.RED_STAINED_GLASS,
			Items.BLACK_STAINED_GLASS);
		tag(Snowmancy.CAN_BE_USED_AS_WEAPON).addTag(ItemTags.SWORDS).add(
			Items.BOW,
			Items.EGG,
			Items.SNOWBALL);
	}
}
