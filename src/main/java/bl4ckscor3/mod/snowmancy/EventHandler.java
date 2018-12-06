package bl4ckscor3.mod.snowmancy;

import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
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
}
