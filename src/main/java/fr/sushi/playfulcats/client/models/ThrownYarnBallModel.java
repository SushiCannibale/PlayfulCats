package fr.sushi.playfulcats.client.models;

import fr.sushi.playfulcats.PlayfulCats;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class ThrownYarnBallModel extends EntityModel<EntityRenderState>
{
	public static final ModelLayerLocation MODEL_LAYER_LOCATION =
			new ModelLayerLocation(
					ResourceLocation.fromNamespaceAndPath(PlayfulCats.MODID,
														  "yarn_ball"),
					"main");
	private final ModelPart bb_main;

	public ThrownYarnBallModel(ModelPart root)
	{
		super(root);
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main",
																  CubeListBuilder
																		  .create()
																		  .texOffs(
																				  0,
																				  0)
																		  .addBox(-3.0F,
																				  0.0F,
																				  -3.0F,
																				  6.0F,
																				  6.0F,
																				  6.0F),
																  PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(EntityRenderState renderState)
	{
		super.setupAnim(renderState);
	}
}