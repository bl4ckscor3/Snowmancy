package bl4ckscor3.mod.snowmancy.renderer;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import bl4ckscor3.mod.snowmancy.model.ModelSnowmanCompanion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderSnowmanCompanion extends LivingRenderer<EntitySnowmanCompanion,ModelSnowmanCompanion>
{
	private static final ResourceLocation SNOWMAN_TEXTURE = new ResourceLocation(Snowmancy.MODID, "textures/entity/snowman.png");
	private static final ResourceLocation SNOWMAN_TEXTURE_GOLDEN = new ResourceLocation(Snowmancy.MODID, "textures/entity/snowman_golden.png");

	public RenderSnowmanCompanion(EntityRendererManager manager)
	{
		super(manager, new ModelSnowmanCompanion(), 0.25F);
	}

	@Override
	protected boolean func_177070_b(EntitySnowmanCompanion entity)
	{
		return entity.hasCustomName();
	}

	@Override
	protected ResourceLocation func_110775_a(EntitySnowmanCompanion entity)
	{
		return entity.isNoseGolden() ? SNOWMAN_TEXTURE_GOLDEN : SNOWMAN_TEXTURE;
	}
}
