package bl4ckscor3.mod.snowmancy.model;

import com.mojang.blaze3d.platform.GlStateManager;

import bl4ckscor3.mod.snowmancy.entity.EntitySnowmanCompanion;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;

//vanilla snowman model with added nose
public class ModelSnowmanCompanion extends EntityModel<EntitySnowmanCompanion>
{
	public RendererModel body;
	public RendererModel bottomBody;
	public RendererModel head;
	public RendererModel rightHand;
	public RendererModel leftHand;
	public RendererModel nose;

	public ModelSnowmanCompanion()
	{
		textureHeight = 64;
		textureWidth = 64;
		head = new RendererModel(this, 0, 0).setTextureSize(64, 64);
		head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, -0.5F);
		head.setRotationPoint(0.0F, 4.0F, 0.0F);
		nose = new RendererModel(this, 56, 60);
		nose.setRotationPoint(-3.0F, 0.0F, 0.0F);
		nose.addBox(2.5F, -4.5F, -6.5F, 1, 1, 3, 0.0F);
		rightHand = new RendererModel(this, 32, 0).setTextureSize(64, 64);
		rightHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, -0.5F);
		rightHand.setRotationPoint(0.0F, 6.0F, 0.0F);
		leftHand = new RendererModel(this, 32, 0).setTextureSize(64, 64);
		leftHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, -0.5F);
		leftHand.setRotationPoint(0.0F, 6.0F, 0.0F);
		body = new RendererModel(this, 0, 16).setTextureSize(64, 64);
		body.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, -0.5F);
		body.setRotationPoint(0.0F, 13.0F, 0.0F);
		bottomBody = new RendererModel(this, 0, 36).setTextureSize(64, 64);
		bottomBody.addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12, -0.5F);
		bottomBody.setRotationPoint(0.0F, 24.0F, 0.0F);
		head.addChild(nose);
	}

	@Override
	public void func_212844_a_(EntitySnowmanCompanion entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
	{
		float f;
		float f1;

		super.func_212844_a_(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

		head.rotateAngleY = netHeadYaw * 0.017453292F;
		head.rotateAngleX = headPitch * 0.017453292F;
		body.rotateAngleY = netHeadYaw * 0.017453292F * 0.25F;
		f = MathHelper.sin(body.rotateAngleY);
		f1 = MathHelper.cos(body.rotateAngleY);
		rightHand.rotateAngleZ = 1.0F;
		leftHand.rotateAngleZ = -1.0F;
		rightHand.rotateAngleY = 0.0F + body.rotateAngleY;
		leftHand.rotateAngleY = (float)Math.PI + body.rotateAngleY;
		rightHand.rotationPointX = f1 * 5.0F;
		rightHand.rotationPointZ = -f * 5.0F;
		leftHand.rotationPointX = -f1 * 5.0F;
		leftHand.rotationPointZ = f * 5.0F;
	}

	@Override
	public void func_78088_a(EntitySnowmanCompanion entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		GlStateManager.translatef(0.0F, 0.75F, 0.0F);
		scale = scale / 2;
		func_212844_a_(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		body.render(scale);
		bottomBody.render(scale);
		head.render(scale);
		rightHand.render(scale);
		leftHand.render(scale);
	}
}