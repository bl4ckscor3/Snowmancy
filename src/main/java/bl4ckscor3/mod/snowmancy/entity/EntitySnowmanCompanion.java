package bl4ckscor3.mod.snowmancy.entity;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.ai.SnowmanAIAttackMelee;
import bl4ckscor3.mod.snowmancy.entity.ai.SnowmanAIAttackRanged;
import bl4ckscor3.mod.snowmancy.util.EnumAttackType;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.TempCategory;

public class EntitySnowmanCompanion extends GolemEntity implements IRangedAttackMob
{
	//TODO: add wearables
	private static final DataParameter<Boolean> GOLDEN_NOSE = EntityDataManager.<Boolean>createKey(EntitySnowmanCompanion.class, DataSerializers.BOOLEAN);
	private static final DataParameter<String> ATTACK_TYPE = EntityDataManager.<String>createKey(EntitySnowmanCompanion.class, DataSerializers.STRING);
	private static final DataParameter<Float> DAMAGE = EntityDataManager.<Float>createKey(EntitySnowmanCompanion.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> EVERCOLD = EntityDataManager.<Boolean>createKey(EntitySnowmanCompanion.class, DataSerializers.BOOLEAN);

	public EntitySnowmanCompanion(EntityType<EntitySnowmanCompanion> type, World world)
	{
		super(type, world);
	}

	public EntitySnowmanCompanion(World world, boolean goldenNose, String attackType, float damage, boolean evercold)
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
		goalSelector.addGoal(1, new SnowmanAIAttackMelee(this));
		goalSelector.addGoal(2, new SnowmanAIAttackRanged(this));
		goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 1.0000001E-5F));
		goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		goalSelector.addGoal(6, new LookRandomlyGoal(this));
		targetSelector.addGoal(1, new NearestAttackableTargetGoal<MobEntity>(this, MobEntity.class, 10, true, false, e -> e instanceof IMob));
	}

	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
		getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
	}

	@Override
	public void livingTick()
	{
		super.livingTick();

		if(!isEvercold() && world.func_226691_t_(getPosition()).getTempCategory() != TempCategory.COLD)
			attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
	}

	@Override
	protected boolean processInteract(PlayerEntity player, Hand hand)
	{
		if(player.isCrouching() && hand == Hand.MAIN_HAND)
		{
			Block.spawnAsEntity(world, getPosition(), createItem());
			remove();
		}

		return super.processInteract(player, hand);
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
		Entity throwableEntity;

		switch(type)
		{
			case ARROW: throwableEntity = ((ArrowItem)Items.ARROW).createArrow(world, new ItemStack(Items.ARROW, 1), this); break;
			case EGG: throwableEntity = new EggEntity(world, this); break;
			case SNOWBALL: throwableEntity = new SnowballEntity(world, this); break;
			default: return;
		}

		double d0 = target.func_226278_cu_() + target.getEyeHeight() - 1.100000023841858D;
		double d1 = target.func_226277_ct_() - func_226277_ct_();
		double d2 = d0 - throwableEntity.func_226278_cu_();
		double d3 = target.func_226281_cx_() - func_226281_cx_();
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;

		((IProjectile)throwableEntity).shoot(d1, d2 + f, d3, 1.6F, 12.0F);
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
