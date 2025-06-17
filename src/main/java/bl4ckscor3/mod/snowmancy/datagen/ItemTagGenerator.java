package bl4ckscor3.mod.snowmancy.datagen;

import java.util.concurrent.CompletableFuture;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider;

public class ItemTagGenerator extends BlockTagCopyingItemTagProvider {
	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
		super(output, lookupProvider, blockTags, Snowmancy.MODID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		//@formatter:off
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
	}
}
