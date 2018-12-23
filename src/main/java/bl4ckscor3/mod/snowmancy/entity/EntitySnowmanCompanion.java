package bl4ckscor3.mod.snowmancy.entity;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntitySnowmanCompanion extends EntityGolem
{
	//TODO: add weapon and wearables
	private static final DataParameter<Boolean> GOLDEN_NOSE = EntityDataManager.<Boolean>createKey(EntitySnowmanCompanion.class, DataSerializers.BOOLEAN);

	public EntitySnowmanCompanion(World world)
	{
		super(world);
		setSize(0.35F, 0.9F);
	}

	public EntitySnowmanCompanion(World world, boolean goldenNose)
	{
		super(world);
		setSize(0.35F, 0.9F);
		dataManager.set(GOLDEN_NOSE, goldenNose);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(GOLDEN_NOSE, false);
	}

	@Override
	protected void initEntityAI()
	{
		tasks.addTask(1, new EntityAIWanderAvoidWater(this, 1.0D, 1.0000001E-5F));
		tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		tasks.addTask(3, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		if(player.isSneaking() && hand == EnumHand.MAIN_HAND)
		{
			ItemStack stack = new ItemStack(Snowmancy.FROZEN_SNOWMAN);
			NBTTagCompound tag = new NBTTagCompound();

			//TODO: write more entity data to item
			tag.setBoolean("goldenCarrot", isNoseGolden());
			stack.setTagCompound(tag);
			Block.spawnAsEntity(world, getPosition(), stack);
			setDead();
		}

		return super.processInteract(player, hand);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		dataManager.set(GOLDEN_NOSE, tag.getBoolean("goldenNose"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		tag.setBoolean("goldenNose", isNoseGolden());
	}

	/**
	 * @return true if this snowman has been created with a golden nose, false otherwhise
	 */
	public boolean isNoseGolden()
	{
		return dataManager.get(GOLDEN_NOSE);
	}
}
