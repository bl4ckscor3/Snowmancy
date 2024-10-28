package bl4ckscor3.mod.snowmancy.client;

import com.mojang.blaze3d.vertex.PoseStack;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanion;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SnowmanCompanionRenderer extends MobRenderer<SnowmanCompanion, SnowmanCompanionRenderState, SnowmanCompanionModel> {
	private static final ResourceLocation SNOWMAN_TEXTURE = ResourceLocation.fromNamespaceAndPath(Snowmancy.MODID, "textures/entity/snowman.png");
	private static final ResourceLocation SNOWMAN_TEXTURE_GOLDEN = ResourceLocation.fromNamespaceAndPath(Snowmancy.MODID, "textures/entity/snowman_golden.png");

	public SnowmanCompanionRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new SnowmanCompanionModel(ctx.bakeLayer(ClientReg.SNOWMAN_LOCATION)), 0.25F);
	}

	@Override
	public void render(SnowmanCompanionRenderState renderState, PoseStack pose, MultiBufferSource bufferSource, int packedLight) {
		pose.scale(0.5F, 0.5F, 0.5F);
		super.render(renderState, pose, bufferSource, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(SnowmanCompanionRenderState renderState) {
		return renderState.goldenCarrot ? SNOWMAN_TEXTURE_GOLDEN : SNOWMAN_TEXTURE;
	}

	@Override
	public void extractRenderState(SnowmanCompanion entity, SnowmanCompanionRenderState renderState, float partialTicks) {
		renderState.goldenCarrot = entity.getSnowmanData().goldenCarrot();
		super.extractRenderState(entity, renderState, partialTicks);
	}

	@Override
	public SnowmanCompanionRenderState createRenderState() {
		return new SnowmanCompanionRenderState();
	}
}
