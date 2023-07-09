package to.tinypota.railbots.registry.client;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class RailbotsParticleTypes {
	public static final DefaultParticleType FLOO_FLAME = register("floo_flame", FabricParticleTypes.simple());
	
	public static void init() {
	
	}
	
	public static <P extends ParticleType<?>> P register(String name, P particleType) {
		return Registry.register(Registries.PARTICLE_TYPE, to.tinypota.railbots.Railbots.id(name), particleType);
	}
}
