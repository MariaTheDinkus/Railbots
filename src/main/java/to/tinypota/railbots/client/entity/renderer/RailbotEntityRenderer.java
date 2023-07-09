package to.tinypota.railbots.client.entity.renderer;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import to.tinypota.railbots.common.entity.RailbotEntity;

public class RailbotEntityRenderer extends EntityRenderer<RailbotEntity> {
	private static final Identifier TEXTURE = new Identifier("textures/entity/slime/slime.png");
	
	public RailbotEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}
	
	@Override
	public Identifier getTexture(RailbotEntity entity) {
		return TEXTURE;
	}
	
	@Override
	public void render(RailbotEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		
		var scale = 1.25F;
		matrices.scale(scale, scale, scale);
		matrices.translate(0.0F, entity.getHeight() / 2 / scale, 0.0F);
		
		var itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		itemRenderer.renderItem(Blocks.SLIME_BLOCK.asItem().getDefaultStack(), ModelTransformationMode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
		
		matrices.pop();
	}
}
