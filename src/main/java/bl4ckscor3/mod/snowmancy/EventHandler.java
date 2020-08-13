package bl4ckscor3.mod.snowmancy;

import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanionEntity;
import bl4ckscor3.mod.snowmancy.tileentity.SnowmanBuilderTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
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
		if(event.getThrowable() instanceof SnowballEntity && event.getRayTraceResult().getType() == Type.BLOCK)
		{
			TileEntity te = event.getThrowable().world.getTileEntity(((BlockRayTraceResult)event.getRayTraceResult()).getPos());

			if(te instanceof SnowmanBuilderTileEntity)
			{
				if(((SnowmanBuilderTileEntity)te).isCraftReady() && ((SnowmanBuilderTileEntity)te).getProgress() < ((SnowmanBuilderTileEntity)te).getMaxProgress())
					te.getWorld().playSound(null, te.getPos(), EGG_SOUND, SoundCategory.BLOCKS, 1.0F, 1.0F);

				((SnowmanBuilderTileEntity)te).increaseProgress();
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if(event.getEntityLiving() instanceof SnowmanCompanionEntity)
			Block.spawnAsEntity(event.getEntityLiving().world, event.getEntityLiving().getPosition(), ((SnowmanCompanionEntity)event.getEntityLiving()).createItem());
	}
}
