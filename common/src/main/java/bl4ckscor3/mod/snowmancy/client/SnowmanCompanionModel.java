package bl4ckscor3.mod.snowmancy.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

// vanilla snowman model with added nose
public class SnowmanCompanionModel extends EntityModel<SnowmanCompanionRenderState> {
	private ModelPart body;
	private ModelPart head;
	private ModelPart rightHand;
	private ModelPart leftHand;

	public SnowmanCompanionModel(ModelPart root) {
		super(root);
		body = root.getChild("body");
		head = root.getChild("head");
		rightHand = root.getChild("right_hand");
		leftHand = root.getChild("left_hand");
	}

	public static LayerDefinition createLayer() {
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
	public void setupAnim(SnowmanCompanionRenderState renderState) {
		float f;
		float f1;

		head.yRot = renderState.yRot * (float) (Math.PI / 180.0);
		head.xRot = renderState.xRot * (float) (Math.PI / 180.0);
		body.yRot = renderState.yRot * (float) (Math.PI / 180.0) * 0.25F;
		f = Mth.sin(body.yRot);
		f1 = Mth.cos(body.yRot);
		rightHand.zRot = 1.0F;
		leftHand.zRot = -1.0F;
		rightHand.yRot = body.yRot;
		leftHand.yRot = (float) Math.PI + body.yRot;
		rightHand.x = f1 * 5.0F;
		rightHand.z = -f * 5.0F;
		leftHand.x = -f1 * 5.0F;
		leftHand.z = f * 5.0F;
	}
}