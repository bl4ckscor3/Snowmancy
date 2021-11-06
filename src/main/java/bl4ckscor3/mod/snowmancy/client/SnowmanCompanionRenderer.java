package bl4ckscor3.mod.snowmancy.client;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanion;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SnowmanCompanionRenderer extends MobRenderer<SnowmanCompanion,SnowmanCompanionModel>
{
	private static final ResourceLocation SNOWMAN_TEXTURE = new ResourceLocation(Snowmancy.MODID, "textures/entity/snowman.png");
	private static final ResourceLocation SNOWMAN_TEXTURE_GOLDEN = new ResourceLocation(Snowmancy.MODID, "textures/entity/snowman_golden.png");

	public SnowmanCompanionRenderer(EntityRendererProvider.Context ctx)
	{
		super(ctx, new SnowmanCompanionModel(ctx.bakeLayer(ClientReg.SNOWMAN_LOCATION)), 0.25F);
	}

	@Override
	public ResourceLocation getTextureLocation(SnowmanCompanion entity)
	{
		return entity.isNoseGolden() ? SNOWMAN_TEXTURE_GOLDEN : SNOWMAN_TEXTURE;
	}
}
