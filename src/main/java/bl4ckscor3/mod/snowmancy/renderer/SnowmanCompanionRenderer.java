package bl4ckscor3.mod.snowmancy.renderer;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanionEntity;
import bl4ckscor3.mod.snowmancy.model.SnowmanCompanionModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class SnowmanCompanionRenderer extends LivingRenderer<SnowmanCompanionEntity,SnowmanCompanionModel>
{
	private static final ResourceLocation SNOWMAN_TEXTURE = new ResourceLocation(Snowmancy.MODID, "textures/entity/snowman.png");
	private static final ResourceLocation SNOWMAN_TEXTURE_GOLDEN = new ResourceLocation(Snowmancy.MODID, "textures/entity/snowman_golden.png");

	public SnowmanCompanionRenderer(EntityRendererManager manager)
	{
		super(manager, new SnowmanCompanionModel(), 0.25F);
	}

	@Override
	protected boolean canRenderName(SnowmanCompanionEntity entity)
	{
		return entity.hasCustomName();
	}

	@Override
	public ResourceLocation getEntityTexture(SnowmanCompanionEntity entity)
	{
		return entity.isNoseGolden() ? SNOWMAN_TEXTURE_GOLDEN : SNOWMAN_TEXTURE;
	}
}
