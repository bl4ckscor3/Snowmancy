package bl4ckscor3.mod.snowmancy.tileentity;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.item.ItemBlockEvercoldIce;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import bl4ckscor3.snowmancy.inventory.InventorySnowmanBuilder;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntitySnowmanBuilder extends TileEntity implements ITickable
{
	private InventorySnowmanBuilder inventory = new InventorySnowmanBuilder(this);
	private byte progress = 0;
	private final byte maxProgress = 8;

	@Override
	public void update()
	{
		if(canOperate())
		{
			for(int i = 0; i < inventory.getSizeInventory() - 1; i++) //last slot is output
			{
				if(inventory.getStackInSlot(i).isEmpty())
				{
					inventory.getItemHandler().setStackInSlot(inventory.getSizeInventory() - 1, ItemStack.EMPTY);
					resetProgress();
					return;
				}
			}

			if(!isCraftReady())
			{
				ItemStack stack = new ItemStack(Snowmancy.FROZEN_SNOWMAN);
				Item weapon = inventory.getStackInSlot(inventory.getSizeInventory() - 2).getItem();
				NBTTagCompound tag = new NBTTagCompound();
				EnumAttackType attackType = (weapon == Items.BOW ? EnumAttackType.ARROW :
					(weapon == Items.EGG ? EnumAttackType.EGG :
						(weapon == Items.SNOWBALL ? EnumAttackType.SNOWBALL : EnumAttackType.HIT)));

				tag.setBoolean("goldenCarrot", inventory.getStackInSlot(1).getItem() == Items.GOLDEN_CARROT);
				tag.setString("attackType", attackType.name());
				tag.setFloat("damage", attackType == EnumAttackType.HIT && weapon instanceof ItemSword ? 4.0F + ((ItemSword)weapon).getAttackDamage() : 0.0F);
				tag.setBoolean("evercold", inventory.getStackInSlot(0).getItem() instanceof ItemBlockEvercoldIce);
				stack.setTagCompound(tag);
				inventory.getItemHandler().setStackInSlot(inventory.getSizeInventory() - 1, stack);
			}
		}
	}

	/**
	 * Increases the progress of the current craft (if applicable) by one
	 */
	public void increaseProgress()
	{
		if(isCraftReady() && progress < maxProgress)
		{
			progress++;
			markDirtyClient();
		}
	}

	/**
	 * Resets the progress of the ongoing craft, probably because it got aborted or was finished
	 */
	public void resetProgress()
	{
		progress = 0;
		markDirtyClient();
	}

	/**
	 * Checks if the crafting status is ready by checking if the last slot (output slot) of this tile's inventory is not empty
	 * @return true if the crafting status is ready, false otherwhise
	 */
	public boolean isCraftReady()
	{
		return !inventory.getStackInSlot(inventory.getSizeInventory() - 1).isEmpty();
	}

	/**
	 * @return true if the machine can work in the current climate, false otherwhise
	 */
	public boolean canOperate()
	{
		int cooling = 0;
		boolean cold = getWorld().getBiome(pos).getTempCategory() == TempCategory.COLD;
		boolean medium = getWorld().getBiome(pos).getTempCategory() == TempCategory.MEDIUM;
		boolean ocean = getWorld().getBiome(pos).getTempCategory() == TempCategory.OCEAN;
		boolean warm = getWorld().getBiome(pos).getTempCategory() == TempCategory.WARM;

		for(EnumFacing facing : EnumFacing.VALUES)
		{
			if(getWorld().getBlockState(pos.offset(facing)).getBlock() == Snowmancy.EVERCOLD_ICE)
				cooling++;
		}
		switch(cooling)
		{
			case 0: case 1: return cold;
			case 2: case 3: return cold || medium || ocean;
			default: return cold || medium || ocean || warm;
		}
	}

	/**
	 * Used server-side whenever the tile entity changes in a way that requires the client to know
	 */
	public void markDirtyClient()
	{
		markDirty();

		if(world != null)
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		NBTTagCompound invTag = (NBTTagCompound)tag.getTag("SnowmanBuilderInventory");

		if(invTag != null)
		{
			for(int i = 0; i < inventory.getContents().size(); i++)
			{
				if(invTag.hasKey("Slot" + i))
					inventory.setInventorySlotContents(i, new ItemStack((NBTTagCompound)invTag.getTag("Slot" + i)));
			}

			progress = tag.getByte("progress");
		}

		super.readFromNBT(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		NBTTagCompound invTag = new NBTTagCompound();

		for(int i = 0; i < inventory.getContents().size(); i++)
		{
			invTag.setTag("Slot" + i, inventory.getStackInSlot(i).writeToNBT(new NBTTagCompound()));
		}

		tag.setTag("SnowmanBuilderInventory", invTag);
		tag.setByte("progress", progress);
		return super.writeToNBT(tag);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 1, writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		progress = pkt.getNbtCompound().getByte("progress");
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

	/**
	 * Gets the crafting progress
	 * @return The crafting progress
	 */
	public byte getProgress()
	{
		return progress;
	}

	/**
	 * Gets the crafting progress' maximum progress (upon which the craft will be completed)
	 * @return The crafting progress' maximum progress
	 */
	public byte getMaxProgress()
	{
		return maxProgress;
	}
}
