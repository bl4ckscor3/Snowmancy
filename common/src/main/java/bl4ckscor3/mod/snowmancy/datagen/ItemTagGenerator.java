package bl4ckscor3.mod.snowmancy.datagen;

import java.util.concurrent.CompletableFuture;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.references.BlockItemId;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.ItemTags;

public class ItemTagGenerator extends VanillaItemTagsProvider {
	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(Snowmancy.STAINED_GLASS_BLOCKS).addAll(BlockItemIds.STAINED_GLASS.map(BlockItemId::item));
		tag(Snowmancy.CAN_BE_USED_AS_WEAPON).addTag(ItemTags.SWORDS).add(
			ItemIds.BOW,
			ItemIds.EGG,
			ItemIds.SNOWBALL,
			ItemIds.WIND_CHARGE);
	}
}
