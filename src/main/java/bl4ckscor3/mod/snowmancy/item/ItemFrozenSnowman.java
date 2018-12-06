package bl4ckscor3.mod.snowmancy.item;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.item.Item;

public class ItemFrozenSnowman extends Item
{
	public static final String NAME = "frozen_snowman";

	public ItemFrozenSnowman()
	{
		super();

		setRegistryName(NAME);
		setTranslationKey(Snowmancy.PREFIX + NAME);
	}
}
