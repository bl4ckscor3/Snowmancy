package bl4ckscor3.mod.snowmancy.tileentity;

import bl4ckscor3.snowmancy.inventory.InventorySnowmanBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntitySnowmanBuilder extends TileEntity
{
	private InventorySnowmanBuilder inventory = new InventorySnowmanBuilder(this);

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		NBTTagCompound invTag = (NBTTagCompound)compound.getTag("SnowmanBuilderInventory");

		for(int i = 0; i < inventory.getContents().size(); i++)
		{
			if(invTag.hasKey("Slot" + i))
				inventory.setInventorySlotContents(i, new ItemStack((NBTTagCompound)invTag.getTag("Slot" + i)));
		}

		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		NBTTagCompound invTag = new NBTTagCompound();

		for(int i = 0; i < inventory.getContents().size(); i++)
		{
			invTag.setTag("Slot" + i, inventory.getStackInSlot(i).writeToNBT(new NBTTagCompound()));
		}

		compound.setTag("SnowmanBuilderInventory", invTag);
		return super.writeToNBT(compound);
	}

	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(cap, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing facing)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) inventory.getItemHandler();
		return super.getCapability(cap, facing);
	}

	/**
	 * @return This tile's inventory
	 */
	public InventorySnowmanBuilder getInventory()
	{
		return inventory;
	}
}
