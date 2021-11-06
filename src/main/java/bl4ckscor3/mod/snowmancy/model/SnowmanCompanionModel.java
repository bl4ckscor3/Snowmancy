package bl4ckscor3.mod.snowmancy.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanionEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

//vanilla snowman model with added nose
public class SnowmanCompanionModel extends EntityModel<SnowmanCompanionEntity>
{
	public ModelPart body;
	public ModelPart bottomBody;
	public ModelPart head;
	public ModelPart rightHand;
	public ModelPart leftHand;
	public ModelPart nose;

	public SnowmanCompanionModel()
	{
		texHeight = 64;
		texWidth = 64;
		head = new ModelPart(this, 0, 0).setTexSize(64, 64);
		head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
		head.setPos(0.0F, 4.0F, 0.0F);
		nose = new ModelPart(this, 56, 60);
		nose.setPos(-3.0F, 0.0F, 0.0F);
		nose.addBox(2.5F, -4.5F, -6.5F, 1, 1, 3);
		rightHand = new ModelPart(this, 32, 0).setTexSize(64, 64);
		rightHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2);
		rightHand.setPos(0.0F, 6.0F, 0.0F);
		leftHand = new ModelPart(this, 32, 0).setTexSize(64, 64);
		leftHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2);
		leftHand.setPos(0.0F, 6.0F, 0.0F);
		body = new ModelPart(this, 0, 16).setTexSize(64, 64);
		body.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10);
		body.setPos(0.0F, 13.0F, 0.0F);
		bottomBody = new ModelPart(this, 0, 36).setTexSize(64, 64);
		bottomBody.addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12);
		bottomBody.setPos(0.0F, 24.0F, 0.0F);
		head.addChild(nose);
	}

	@Override
	public void setupAnim(SnowmanCompanionEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		float f;
		float f1;

		head.yRot = netHeadYaw * 0.017453292F;
		head.xRot = headPitch * 0.017453292F;
		body.yRot = netHeadYaw * 0.017453292F * 0.25F;
		f = Mth.sin(body.yRot);
		f1 = Mth.cos(body.yRot);
		rightHand.zRot = 1.0F;
		leftHand.zRot = -1.0F;
		rightHand.yRot = 0.0F + body.yRot;
		leftHand.yRot = (float)Math.PI + body.yRot;
		rightHand.x = f1 * 5.0F;
		rightHand.z = -f * 5.0F;
		leftHand.x = -f1 * 5.0F;
		leftHand.z = f * 5.0F;
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
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