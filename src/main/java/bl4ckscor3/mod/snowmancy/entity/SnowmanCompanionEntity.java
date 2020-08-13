package bl4ckscor3.mod.snowmancy.entity;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.goal.SnowmanAttackMeleeGoal;
import bl4ckscor3.mod.snowmancy.entity.goal.SnowmanAttackRangedGoal;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap.MutableAttribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SnowmanCompanionEntity extends GolemEntity implements IRangedAttackMob
{
	//TODO: add wearables
	private static final DataParameter<Boolean> GOLDEN_NOSE = EntityDataManager.<Boolean>createKey(SnowmanCompanionEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<String> ATTACK_TYPE = EntityDataManager.<String>createKey(SnowmanCompanionEntity.class, DataSerializers.STRING);
	private static final DataParameter<Float> DAMAGE = EntityDataManager.<Float>createKey(SnowmanCompanionEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> EVERCOLD = EntityDataManager.<Boolean>createKey(SnowmanCompanionEntity.class, DataSerializers.BOOLEAN);

	public SnowmanCompanionEntity(EntityType<SnowmanCompanionEntity> type, World world)
	{
		super(type, world);
	}

	public SnowmanCompanionEntity(World world, boolean goldenNose, String attackType, float damage, boolean evercold)
	{
		this(Snowmancy.eTypeSnowman, world);
		dataManager.set(GOLDEN_NOSE, goldenNose);
		dataManager.set(ATTACK_TYPE, attackType);
		dataManager.set(DAMAGE, damage);
		dataManager.set(EVERCOLD, evercold);
	}

	@Override
	protected void registerData()
	{
		super.registerData();
		dataManager.register(GOLDEN_NOSE, false);
		dataManager.register(ATTACK_TYPE, EnumAttackType.HIT.name());
		dataManager.register(DAMAGE, 0.0F);
		dataManager.register(EVERCOLD, false);
	}

	@Override
	protected void registerGoals()
	{
		goalSelector.addGoal(1, new SnowmanAttackMeleeGoal(this));
		goalSelector.addGoal(2, new SnowmanAttackRangedGoal(this));
		goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 1.0000001E-5F));
		goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		goalSelector.addGoal(6, new LookRandomlyGoal(this));
		targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, MobEntity.class, 10, true, false, e -> e instanceof IMob));
	}

	public static MutableAttribute getAttributes()
	{
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 4.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D);
	}

	@Override
	public void livingTick()
	{
		super.livingTick();

		if(!isEvercold() && world.getBiome(getPosition()).func_242445_k() >= 0.2F)
			attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
	}

	@Override
	protected ActionResultType func_230254_b_(PlayerEntity player, Hand hand) //processInteract
	{
		if(player.isCrouching() && hand == Hand.MAIN_HAND)
		{
			Block.spawnAsEntity(world, getPosition(), createItem());
			remove();
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	/**
	 * @return the item from this entity with which this entity can be spawned again
	 */
	public ItemStack createItem()
	{
		ItemStack stack = new ItemStack(Snowmancy.FROZEN_SNOWMAN);
		CompoundNBT tag = new CompoundNBT();

		writeAdditional(tag);
		stack.setTag(tag);
		return stack;
	}

	@Override
	public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor)
	{
		EnumAttackType type = EnumAttackType.valueOf(getAttackType());
		ProjectileEntity throwableEntity;

		switch(type)
		{
			case ARROW: throwableEntity = ((ArrowItem)Items.ARROW).createArrow(world, new ItemStack(Items.ARROW, 1), this); break;
			case EGG: throwableEntity = new EggEntity(world, this); break;
			case SNOWBALL: throwableEntity = new SnowballEntity(world, this); break;
			default: return;
		}

		double d0 = target.getPosY() + target.getEyeHeight() - 1.100000023841858D;
		double d1 = target.getPosX() - getPosX();
		double d2 = d0 - throwableEntity.getPosY();
		double d3 = target.getPosZ() - getPosZ();
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;

		throwableEntity.shoot(d1, d2 + f, d3, 1.6F, 12.0F);
		playSound(SoundEvents.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
		world.addEntity(throwableEntity);
	}

	@Override
	public void readAdditional(CompoundNBT tag)
	{
		dataManager.set(GOLDEN_NOSE, tag.getBoolean("goldenCarrot"));
		dataManager.set(ATTACK_TYPE, tag.getString("attackType"));
		dataManager.set(DAMAGE, tag.getFloat("damage"));
		dataManager.set(EVERCOLD, tag.getBoolean("evercold"));
	}

	@Override
	public void writeAdditional(CompoundNBT tag)
	{
		tag.putBoolean("goldenCarrot", isNoseGolden());
		tag.putString("attackType", getAttackType());
		tag.putFloat("damage", getDamage());
		tag.putBoolean("evercold", isEvercold());
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
}
