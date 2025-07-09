package fr.sushi.playfulcats.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.sushi.playfulcats.PlayfulCats;
import fr.sushi.playfulcats.client.models.ThrownYarnBallModel;
import fr.sushi.playfulcats.common.entity.ThrownYarnBall;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ThrownYarnBallRenderer extends EntityRenderer<ThrownYarnBall, EntityRenderState>
{
	public static final ResourceLocation TEXTURE =
			ResourceLocation.fromNamespaceAndPath(PlayfulCats.MODID,
												  "textures/entity/yarn_ball/yarn_ball.png");
	private final ThrownYarnBallModel model;

	public ThrownYarnBallRenderer(EntityRendererProvider.Context context)
	{
		super(context);
		ModelPart root = context.getModelSet().bakeLayer(
				ThrownYarnBallModel.MODEL_LAYER_LOCATION);
		this.model = new ThrownYarnBallModel(root);
	}

	@Override
	public @NotNull EntityRenderState createRenderState()
	{
		return new EntityRenderState();
	}

	@Override
	public void render(EntityRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
	{
		super.render(renderState, poseStack, bufferSource, packedLight);
		RenderType renderType = this.model.renderType(TEXTURE);
		VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
		this.model.renderToBuffer(poseStack, vertexConsumer, packedLight,
								  OverlayTexture.NO_OVERLAY);
	}
}
