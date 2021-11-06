package bl4ckscor3.mod.snowmancy;

import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanionEntity;
import bl4ckscor3.mod.snowmancy.tileentity.SnowmanBuilderTileEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid=Snowmancy.MODID)
public class EventHandler
{
	private static final SoundEvent EGG_SOUND = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.chicken.egg"));

	@SubscribeEvent
	public static void onProjectileImpactThrowable(ProjectileImpactEvent.Throwable event)
	{
		if(event.getThrowable() instanceof Snowball && event.getRayTraceResult().getType() == Type.BLOCK)
		{
			BlockEntity te = event.getThrowable().level.getBlockEntity(((BlockHitResult)event.getRayTraceResult()).getBlockPos());

			if(te instanceof SnowmanBuilderTileEntity)
			{
				if(((SnowmanBuilderTileEntity)te).isCraftReady() && ((SnowmanBuilderTileEntity)te).getProgress() < ((SnowmanBuilderTileEntity)te).getMaxProgress())
					te.getLevel().playSound(null, te.getBlockPos(), EGG_SOUND, SoundSource.BLOCKS, 1.0F, 1.0F);

				((SnowmanBuilderTileEntity)te).increaseProgress();
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if(event.getEntityLiving() instanceof SnowmanCompanionEntity)
			Block.popResource(event.getEntityLiving().level, event.getEntityLiving().blockPosition(), ((SnowmanCompanionEntity)event.getEntityLiving()).createItem());
	}
}
