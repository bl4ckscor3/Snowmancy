package bl4ckscor3.mod.snowmancy.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

//vanilla snowman model with added nose
public class ModelSnowmanCompanion extends ModelBase
{
	public ModelRenderer body;
	public ModelRenderer bottomBody;
	public ModelRenderer head;
	public ModelRenderer rightHand;
	public ModelRenderer leftHand;
	public ModelRenderer nose;

	public ModelSnowmanCompanion()
	{
		textureHeight = 64;
		textureWidth = 64;
		head = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
		head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, -0.5F);
		head.setRotationPoint(0.0F, 4.0F, 0.0F);
		nose = new ModelRenderer(this, 56, 60);
		nose.setRotationPoint(-3.0F, 0.0F, 0.0F);
		nose.addBox(2.5F, -4.5F, -6.5F, 1, 1, 3, 0.0F);
		rightHand = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
		rightHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, -0.5F);
		rightHand.setRotationPoint(0.0F, 6.0F, 0.0F);
		leftHand = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
		leftHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, -0.5F);
		leftHand.setRotationPoint(0.0F, 6.0F, 0.0F);
		body = new ModelRenderer(this, 0, 16).setTextureSize(64, 64);
		body.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, -0.5F);
		body.setRotationPoint(0.0F, 13.0F, 0.0F);
		bottomBody = new ModelRenderer(this, 0, 36).setTextureSize(64, 64);
		bottomBody.addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12, -0.5F);
		bottomBody.setRotationPoint(0.0F, 24.0F, 0.0F);
		head.addChild(nose);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity)
	{
		float f;
		float f1;

		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

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
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		GlStateManager.translate(0.0F, 0.75F, 0.0F);
		scale = scale / 2;
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		body.render(scale);
		bottomBody.render(scale);
		head.render(scale);
		rightHand.render(scale);
		leftHand.render(scale);
	}
}