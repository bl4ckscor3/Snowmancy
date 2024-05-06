package bl4ckscor3.mod.snowmancy.entity;

import com.mojang.datafixers.util.Pair;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.item.SnowmanData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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
	public static final EntityDataAccessor<SnowmanData> SNOWMAN_DATA = SynchedEntityData.<SnowmanData>defineId(SnowmanCompanion.class, Snowmancy.SNOWMAN_DATA_SERIALIZER.get());

	public SnowmanCompanion(EntityType<SnowmanCompanion> type, Level world) {
		super(type, world);
	}

	public SnowmanCompanion(Level world, SnowmanData snowmanData) {
		this(Snowmancy.SNOWMAN_ENTITY.get(), world);
		entityData.set(SNOWMAN_DATA, snowmanData);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(SNOWMAN_DATA, SnowmanData.EMPTY);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new SnowmanAttackMeleeGoal(this));
		goalSelector.addGoal(2, new SnowmanAttackRangedGoal(this));
		goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D, 1.0000001E-5F));
		goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, Enemy.class::isInstance));
	}

	public static Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.2D);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData) {
		if (reason == MobSpawnType.COMMAND)
			entityData.set(SNOWMAN_DATA, SnowmanData.random(level.getRandom()));

		return super.finalizeSpawn(level, difficulty, reason, spawnData);
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (!getSnowmanData().evercold() && level().getBiome(blockPosition()).value().getBaseTemperature() >= 0.2F)
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

		stack.set(Snowmancy.SNOWMAN_DATA, entityData.get(SNOWMAN_DATA));
		return stack;
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		AttackType type = getSnowmanData().attackType();
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
		if (tag.contains("snowman_data"))
			entityData.set(SNOWMAN_DATA, SnowmanData.CODEC.decode(NbtOps.INSTANCE, tag).result().orElseGet(() -> Pair.of(SnowmanData.EMPTY, tag)).getFirst());
		else //legacy
			entityData.set(SNOWMAN_DATA, new SnowmanData(AttackType.fromTag(tag), tag.getFloat("damage"), tag.getBoolean("evercold"), tag.getBoolean("goldenCarrot")));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		tag.put("snowman_data", SnowmanData.CODEC.encodeStart(NbtOps.INSTANCE, getSnowmanData()).getOrThrow());
	}

	/**
	 * @return The data of this snowman
	 */
	public SnowmanData getSnowmanData() {
		return entityData.get(SNOWMAN_DATA);
	}
}
