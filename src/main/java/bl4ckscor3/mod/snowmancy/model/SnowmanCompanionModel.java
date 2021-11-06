package bl4ckscor3.mod.snowmancy.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import bl4ckscor3.mod.snowmancy.entity.SnowmanCompanionEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
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

	public SnowmanCompanionModel(ModelPart modelPart)
	{
		body = modelPart.getChild("body");
		bottomBody = modelPart.getChild("neck");
		head = modelPart.getChild("head");
		rightHand = modelPart.getChild("right_eye");
		leftHand = modelPart.getChild("left_eye");
		nose = head.getChild("nose");
	}

	public static LayerDefinition createLayer()
	{
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();
		PartDefinition headDefinition = partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8), PartPose.offset(0.0F, 4.0F, 0.0F));

		headDefinition.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(56, 60).addBox(2.5F, -4.5F, -6.5F, 1, 1, 3), PartPose.offset(-3.0F, 0.0F, 0.0F));
		partDefinition.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2), PartPose.offset(0.0F, 6.0F, 0.0F));
		partDefinition.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2), PartPose.offset(0.0F, 6.0F, 0.0F));
		partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10), PartPose.offset(0.0F, 13.0F, 0.0F));
		partDefinition.addOrReplaceChild("bottom_body", CubeListBuilder.create().texOffs(0, 36).addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(meshDefinition, 64, 64);
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