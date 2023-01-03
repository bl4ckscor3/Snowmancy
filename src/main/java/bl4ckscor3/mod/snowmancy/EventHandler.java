package bl4ckscor3.mod.snowmancy;

import bl4ckscor3.mod.snowmancy.block.SnowmanBuilderBlockEntity;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanion;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Snowmancy.MODID)
public class EventHandler {
	@SubscribeEvent
	public static void onProjectileImpactThrowable(ProjectileImpactEvent event) {
		if (event.getProjectile() instanceof Snowball snowball && event.getRayTraceResult().getType() == Type.BLOCK) {
			BlockEntity be = snowball.level.getBlockEntity(((BlockHitResult) event.getRayTraceResult()).getBlockPos());

			if (be instanceof SnowmanBuilderBlockEntity builder) {
				if (builder.isCraftReady() && builder.getProgress() < builder.getMaxProgress())
					be.getLevel().playSound(null, be.getBlockPos(), SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 1.0F, 1.0F);

				builder.increaseProgress();
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		if (event.getEntityLiving() instanceof SnowmanCompanion snowman)
			Block.popResource(snowman.level, snowman.blockPosition(), snowman.createItem());
	}
}
