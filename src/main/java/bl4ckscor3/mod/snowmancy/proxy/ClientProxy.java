package bl4ckscor3.mod.snowmancy.proxy;

import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import bl4ckscor3.mod.snowmancy.renderer.RenderSnowmanCompanion;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends ServerProxy
{
	@Override
	public void registerRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySnowmanCompanion.class, new RenderSnowmanCompanion());
	}
}
