package bl4ckscor3.mod.snowmancy.container;

import bl4ckscor3.mod.snowmancy.container.components.SlotRestricted;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import bl4ckscor3.mod.snowmancy.util.ISnowmanWearable;
import bl4ckscor3.mod.snowmancy.util.IStackValidator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ContainerSnowmanBuilder extends Container
{
	public ContainerSnowmanBuilder(InventoryPlayer playerInv, TileEntitySnowmanBuilder te)
	{
		//player inventory
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(playerInv, 9 + j + i * 9, 8 + j * 18, 157 + i * 18));
			}
		}

		//player hotbar
		for(int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 215));
		}

		IStackValidator coalValidator = (stack) -> {
			return Items.COAL.getRegistryName().equals(stack.getItem().getRegistryName()) || OreDictionary.itemMatches(new ItemStack(Items.COAL), stack, false);
		};
		IStackValidator snowValidator = (stack) -> {
			Item snow = Item.getItemFromBlock(Blocks.SNOW);

			return snow.getRegistryName().equals(stack.getItem().getRegistryName()) || OreDictionary.itemMatches(new ItemStack(snow), stack, false);
		};

		//hat slot
		addSlotToContainer(new SlotRestricted(te.getInventory(), 0, 80, 7, 1, (stack) -> {
			return stack.getItem() instanceof ISnowmanWearable;
		}));
		//left eye slot
		addSlotToContainer(new SlotRestricted(te.getInventory(), 1, 59, 18, 1, coalValidator));
		//nose slot
		addSlotToContainer(new SlotRestricted(te.getInventory(), 2, 80, 28, 1, (stack) -> {
			return Items.CARROT.getRegistryName().equals(stack.getItem().getRegistryName()) || OreDictionary.itemMatches(new ItemStack(Items.CARROT), stack, false);
		}));
		//right eye slot
		addSlotToContainer(new SlotRestricted(te.getInventory(), 3, 101, 18, 1, coalValidator));
		//mouth slots (left to right)
		addSlotToContainer(new SlotRestricted(te.getInventory(), 4, 38, 38, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), 5, 59, 49, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), 6, 80, 49, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), 7, 101, 49, 1, coalValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), 8, 122, 38, 1, coalValidator));
		//body slots (top to bottom)
		addSlotToContainer(new SlotRestricted(te.getInventory(), 9, 80, 74, 1, snowValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), 10, 80, 103, 1, snowValidator));
		addSlotToContainer(new SlotRestricted(te.getInventory(), 11, 80, 132, 1, snowValidator));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
}
