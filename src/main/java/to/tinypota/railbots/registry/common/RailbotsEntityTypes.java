package to.tinypota.railbots.registry.common;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import to.tinypota.railbots.Railbots;
import to.tinypota.railbots.common.entity.RailbotEntity;

public class RailbotsEntityTypes {
	public static final EntityType<RailbotEntity> RAILBOT = register("railbot", FabricEntityTypeBuilder
			.create()
			.<RailbotEntity>entityFactory(RailbotEntity::new)
			.dimensions(EntityDimensions.changing(0.5F, 0.5F))
			.build()
	);
	
	public static void init() {
	
	}
	
	public static <E extends EntityType<?>> E register(String name, E entityType) {
		return Registry.register(Registries.ENTITY_TYPE, Railbots.id(name), entityType);
	}
}
