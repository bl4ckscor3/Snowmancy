package bl4ckscor3.mod.snowmancy;

import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import bl4ckscor3.mod.snowmancy.tileentity.TileEntitySnowmanBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
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
		if(event.getThrowable() instanceof EntitySnowball && event.getRayTraceResult().type == Type.BLOCK)
		{
			TileEntity te = event.getThrowable().world.getTileEntity(event.getRayTraceResult().getBlockPos());

			if(te instanceof TileEntitySnowmanBuilder)
			{
				if(((TileEntitySnowmanBuilder)te).isCraftReady() && ((TileEntitySnowmanBuilder)te).getProgress() < ((TileEntitySnowmanBuilder)te).getMaxProgress())
					te.getWorld().playSound(null, te.getPos(), EGG_SOUND, SoundCategory.BLOCKS, 1.0F, 1.0F);

				((TileEntitySnowmanBuilder)te).increaseProgress();
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if(event.getEntityLiving() instanceof EntitySnowmanCompanion)
			Block.spawnAsEntity(event.getEntityLiving().world, event.getEntityLiving().getPosition(), ((EntitySnowmanCompanion)event.getEntityLiving()).createItem());
	}
}
