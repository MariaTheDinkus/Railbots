package to.tinypota.railbots.registry.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import to.tinypota.railbots.registry.common.RailbotsBlocks;

public class RailbotsBlockRenderLayers {
	public static void init() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), RailbotsBlocks.RAIL);
	}
}
