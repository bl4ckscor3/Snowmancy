package bl4ckscor3.mod.snowmancy.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanionEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

//vanilla snowman model with added nose
public class SnowmanCompanionModel extends EntityModel<SnowmanCompanionEntity>
{
	public ModelRenderer body;
	public ModelRenderer bottomBody;
	public ModelRenderer head;
	public ModelRenderer rightHand;
	public ModelRenderer leftHand;
	public ModelRenderer nose;

	public SnowmanCompanionModel()
	{
		textureHeight = 64;
		textureWidth = 64;
		head = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
		head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
		head.setRotationPoint(0.0F, 4.0F, 0.0F);
		nose = new ModelRenderer(this, 56, 60);
		nose.setRotationPoint(-3.0F, 0.0F, 0.0F);
		nose.addBox(2.5F, -4.5F, -6.5F, 1, 1, 3);
		rightHand = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
		rightHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2);
		rightHand.setRotationPoint(0.0F, 6.0F, 0.0F);
		leftHand = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
		leftHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2);
		leftHand.setRotationPoint(0.0F, 6.0F, 0.0F);
		body = new ModelRenderer(this, 0, 16).setTextureSize(64, 64);
		body.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10);
		body.setRotationPoint(0.0F, 13.0F, 0.0F);
		bottomBody = new ModelRenderer(this, 0, 36).setTextureSize(64, 64);
		bottomBody.addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12);
		bottomBody.setRotationPoint(0.0F, 24.0F, 0.0F);
		head.addChild(nose);
	}

	@Override
	public void setRotationAngles(SnowmanCompanionEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		float f;
		float f1;

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
	public void render(MatrixStack stack, IVertexBuilder builder, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
	{
		stack.translate(0.0D, 0.75D, 0.0D);
		stack.scale(0.5F, 0.5F, 0.5F);
		body.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
		bottomBody.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
		head.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
		rightHand.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
		leftHand.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
	}
}