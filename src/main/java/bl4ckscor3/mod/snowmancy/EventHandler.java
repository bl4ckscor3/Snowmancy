package bl4ckscor3.mod.snowmancy;

import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EventHandler
{
	@SubscribeEvent
	public static void onProjectileImpactThrowable(ProjectileImpactEvent.Throwable event)
	{
		if(event.getThrowable() instanceof EntitySnowball && event.getRayTraceResult().typeOfHit == Type.BLOCK)
		{
			TileEntity te = event.getThrowable().world.getTileEntity(event.getRayTraceResult().getBlockPos());

			if(te instanceof TileEntitySnowmanBuilder)
				((TileEntitySnowmanBuilder)te).increaseProgress();
		}
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if(event.getEntityLiving() instanceof EntitySnowmanCompanion)
			Block.spawnAsEntity(event.getEntityLiving().world, event.getEntityLiving().getPosition(), ((EntitySnowmanCompanion)event.getEntityLiving()).createItem());
	}
}
