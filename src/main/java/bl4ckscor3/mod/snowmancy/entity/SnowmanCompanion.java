package bl4ckscor3.mod.snowmancy.entity;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;

public class SnowmanCompanion extends AbstractGolem implements RangedAttackMob {
	//TODO: add wearables
	private static final EntityDataAccessor<Boolean> GOLDEN_NOSE = SynchedEntityData.<Boolean>defineId(SnowmanCompanion.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<AttackType> ATTACK_TYPE = SynchedEntityData.<AttackType>defineId(SnowmanCompanion.class, Snowmancy.ATTACK_TYPE_SERIALIZER.get());
	private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.<Float>defineId(SnowmanCompanion.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> EVERCOLD = SynchedEntityData.<Boolean>defineId(SnowmanCompanion.class, EntityDataSerializers.BOOLEAN);

	public SnowmanCompanion(EntityType<SnowmanCompanion> type, Level world) {
		super(type, world);
	}

	public SnowmanCompanion(Level world, boolean goldenNose, AttackType attackType, float damage, boolean evercold) {
		this(Snowmancy.SNOWMAN_ENTITY.get(), world);
		setData(goldenNose, attackType, damage, evercold);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(GOLDEN_NOSE, false);
		entityData.define(ATTACK_TYPE, AttackType.NONE);
		entityData.define(DAMAGE, 0.0F);
		entityData.define(EVERCOLD, false);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new SnowmanAttackMeleeGoal(this));
		goalSelector.addGoal(2, new SnowmanAttackRangedGoal(this));
		goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D, 1.0000001E-5F));
		goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, e -> e instanceof Enemy));
	}

	public static Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.2D);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag) {
		if (reason == MobSpawnType.COMMAND) {
			RandomSource random = level.getRandom();
			boolean goldenNose = random.nextBoolean();
			AttackType attackType = AttackType.values()[random.nextInt(AttackType.values().length)];
			float damage = attackType.isMelee() ? random.nextInt(21) + random.nextFloat() : 0.0F;
			boolean evercold = random.nextBoolean();

			setData(goldenNose, attackType, damage, evercold);
		}

		return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (!isEvercold() && level().getBiome(blockPosition()).value().getBaseTemperature() >= 0.2F)
			hurt(damageSources().onFire(), 1.0F);
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (player.isCrouching() && hand == InteractionHand.MAIN_HAND) {
			Block.popResource(level(), blockPosition(), createItem());
			discard();
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	/**
	 * @return the item from this entity with which this entity can be spawned again
	 */
	public ItemStack createItem() {
		ItemStack stack = new ItemStack(Snowmancy.FROZEN_SNOWMAN.get());
		CompoundTag tag = new CompoundTag();

		addAdditionalSaveData(tag);
		stack.setTag(tag);
		return stack;
	}

	public void setData(boolean goldenNose, AttackType attackType, float damage, boolean evercold) {
		entityData.set(GOLDEN_NOSE, goldenNose);
		entityData.set(ATTACK_TYPE, attackType);
		entityData.set(DAMAGE, damage);
		entityData.set(EVERCOLD, evercold);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		AttackType type = getAttackType();
		Projectile throwableEntity = switch (type) {
			case ARROW -> ((ArrowItem) Items.ARROW).createArrow(level(), new ItemStack(Items.ARROW, 1), this);
			case EGG -> new ThrownEgg(level(), this);
			case SNOWBALL -> new Snowball(level(), this);
			default -> null;
		};

		if (throwableEntity != null) {
			double d0 = target.getY() + target.getEyeHeight() - 1.100000023841858D;
			double d1 = target.getX() - getX();
			double d2 = d0 - throwableEntity.getY();
			double d3 = target.getZ() - getZ();
			float f = Mth.sqrt((float) (d1 * d1 + d3 * d3)) * 0.2F;

			throwableEntity.shoot(d1, d2 + f, d3, 1.6F, 12.0F);
			playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
			level().addFreshEntity(throwableEntity);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		entityData.set(GOLDEN_NOSE, tag.getBoolean("goldenCarrot"));
		entityData.set(ATTACK_TYPE, AttackType.fromTag(tag));
		entityData.set(DAMAGE, tag.getFloat("damage"));
		entityData.set(EVERCOLD, tag.getBoolean("evercold"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		tag.putBoolean("goldenCarrot", isNoseGolden());
		tag.putInt("attackType", getAttackType().ordinal());
		tag.putFloat("damage", getDamage());
		tag.putBoolean("evercold", isEvercold());
	}

	/**
	 * @return true if this snowman has been created with a golden nose, false otherwhise
	 */
	public boolean isNoseGolden() {
		return entityData.get(GOLDEN_NOSE);
	}

	/**
	 * @return The attack type of the snowman (does he have to hit or throw?)
	 */
	public AttackType getAttackType() {
		return entityData.get(ATTACK_TYPE);
	}

	/**
	 * @return The damage this snowman does when a hit type weapon is equipped
	 */
	public float getDamage() {
		return entityData.get(DAMAGE);
	}

	/**
	 * @return true if this snowman can live in biomes that are not cold
	 */
	public boolean isEvercold() {
		return entityData.get(EVERCOLD);
	}
}
