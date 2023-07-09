package to.tinypota.railbots.registry.client;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import to.tinypota.railbots.client.entity.renderer.RailbotEntityRenderer;
import to.tinypota.railbots.registry.common.RailbotsEntityTypes;

public class RailbotsEntityRenderers {
	public static void init() {
		EntityRendererRegistry.register(RailbotsEntityTypes.RAILBOT, RailbotEntityRenderer::new);
	}
}
