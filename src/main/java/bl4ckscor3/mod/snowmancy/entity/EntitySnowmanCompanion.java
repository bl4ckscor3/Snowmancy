package bl4ckscor3.mod.snowmancy.entity;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.ai.SnowmanAIAttackMelee;
import bl4ckscor3.mod.snowmancy.entity.ai.SnowmanAIAttackRanged;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.TempCategory;

public class EntitySnowmanCompanion extends EntityGolem implements IRangedAttackMob
{
	//TODO: add wearables
	private static final DataParameter<Boolean> GOLDEN_NOSE = EntityDataManager.<Boolean>createKey(EntitySnowmanCompanion.class, DataSerializers.BOOLEAN);
	private static final DataParameter<String> ATTACK_TYPE = EntityDataManager.<String>createKey(EntitySnowmanCompanion.class, DataSerializers.STRING);
	private static final DataParameter<Float> DAMAGE = EntityDataManager.<Float>createKey(EntitySnowmanCompanion.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> EVERCOLD = EntityDataManager.<Boolean>createKey(EntitySnowmanCompanion.class, DataSerializers.BOOLEAN);

	public EntitySnowmanCompanion(World world)
	{
		super(world);
		setSize(0.35F, 0.9F);
	}

	public EntitySnowmanCompanion(World world, boolean goldenNose, String attackType, float damage, boolean evercold)
	{
		super(world);
		setSize(0.35F, 0.9F);
		dataManager.set(GOLDEN_NOSE, goldenNose);
		dataManager.set(ATTACK_TYPE, attackType);
		dataManager.set(DAMAGE, damage);
		dataManager.set(EVERCOLD, evercold);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(GOLDEN_NOSE, false);
		dataManager.register(ATTACK_TYPE, EnumAttackType.HIT.name());
		dataManager.register(DAMAGE, 0.0F);
		dataManager.register(EVERCOLD, false);
	}

	@Override
	protected void initEntityAI()
	{
		tasks.addTask(1, new SnowmanAIAttackMelee(this));
		tasks.addTask(2, new SnowmanAIAttackRanged(this));
		tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D, 1.0000001E-5F));
		tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		tasks.addTask(6, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityLiving>(this, EntityLiving.class, 10, true, false, IMob.MOB_SELECTOR));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		if(!isEvercold() && world.getBiome(getPosition()).getTempCategory() != TempCategory.COLD)
			attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		if(player.isSneaking() && hand == EnumHand.MAIN_HAND)
		{
			Block.spawnAsEntity(world, getPosition(), createItem());
			setDead();
		}

		return super.processInteract(player, hand);
	}

	/**
	 * @return the item from this entity with which this entity can be spawned again
	 */
	public ItemStack createItem()
	{
		ItemStack stack = new ItemStack(Snowmancy.FROZEN_SNOWMAN);
		NBTTagCompound tag = new NBTTagCompound();

		writeEntityToNBT(tag);
		stack.setTagCompound(tag);
		return stack;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
		EnumAttackType type = EnumAttackType.valueOf(getAttackType());
		Entity throwableEntity;

		switch(type)
		{
			case ARROW: throwableEntity = ((ItemArrow)Items.ARROW).createArrow(world, new ItemStack(Items.ARROW, 1), this); break;
			case EGG: throwableEntity = new EntityEgg(world, this); break;
			case SNOWBALL: throwableEntity = new EntitySnowball(world, this); break;
			default: return;
		}

		double d0 = target.posY + target.getEyeHeight() - 1.100000023841858D;
		double d1 = target.posX - posX;
		double d2 = d0 - throwableEntity.posY;
		double d3 = target.posZ - posZ;
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;

		((IProjectile)throwableEntity).shoot(d1, d2 + f, d3, 1.6F, 12.0F);
		playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
		world.spawnEntity(throwableEntity);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		dataManager.set(GOLDEN_NOSE, tag.getBoolean("goldenCarrot"));
		dataManager.set(ATTACK_TYPE, tag.getString("attackType"));
		dataManager.set(DAMAGE, tag.getFloat("damage"));
		dataManager.set(EVERCOLD, tag.getBoolean("evercold"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		tag.setBoolean("goldenCarrot", isNoseGolden());
		tag.setString("attackType", getAttackType());
		tag.setFloat("damage", getDamage());
		tag.setBoolean("evercold", isEvercold());
	}

	/**
	 * @return true if this snowman has been created with a golden nose, false otherwhise
	 */
	public boolean isNoseGolden()
	{
		return dataManager.get(GOLDEN_NOSE);
	}

	/**
	 * @return The attack type of the snowman (does he have to hit or throw?)
	 */
	public String getAttackType()
	{
		return dataManager.get(ATTACK_TYPE);
	}

	/**
	 * @return The damage this snowman does when a hit type weapon is equipped
	 */
	public float getDamage()
	{
		return dataManager.get(DAMAGE);
	}

	/**
	 * @return true if this snowman can live in biomes that are not cold
	 */
	public boolean isEvercold()
	{
		return dataManager.get(EVERCOLD);
	}


	@Override
	public void setSwingingArms(boolean swingingArms) {}
}
