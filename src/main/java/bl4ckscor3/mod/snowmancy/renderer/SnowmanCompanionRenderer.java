package bl4ckscor3.mod.snowmancy.renderer;

import bl4ckscor3.mod.snowmancy.ClientReg;
import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanionEntity;
import bl4ckscor3.mod.snowmancy.model.SnowmanCompanionModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SnowmanCompanionRenderer extends MobRenderer<SnowmanCompanionEntity,SnowmanCompanionModel>
{
	private static final ResourceLocation SNOWMAN_TEXTURE = new ResourceLocation(Snowmancy.MODID, "textures/entity/snowman.png");
	private static final ResourceLocation SNOWMAN_TEXTURE_GOLDEN = new ResourceLocation(Snowmancy.MODID, "textures/entity/snowman_golden.png");

	public SnowmanCompanionRenderer(EntityRendererProvider.Context ctx)
	{
		super(ctx, new SnowmanCompanionModel(ctx.bakeLayer(ClientReg.SNOWMAN_LOCATION)), 0.25F);
	}

	@Override
	public ResourceLocation getTextureLocation(SnowmanCompanionEntity entity)
	{
		return entity.isNoseGolden() ? SNOWMAN_TEXTURE_GOLDEN : SNOWMAN_TEXTURE;
	}
}
