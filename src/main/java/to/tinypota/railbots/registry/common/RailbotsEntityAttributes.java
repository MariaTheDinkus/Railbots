package to.tinypota.railbots.registry.common;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import to.tinypota.railbots.common.entity.RailbotEntity;

public class RailbotsEntityAttributes {
	public static void init() {
		FabricDefaultAttributeRegistry.register(RailbotsEntityTypes.RAILBOT, RailbotEntity.createMobAttributes());
	}
}
