package to.tinypota.railbots;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import to.tinypota.railbots.registry.client.RailbotsParticleTypes;
import to.tinypota.railbots.registry.common.*;

public class Railbots implements ModInitializer {
	public static final String ID = "railbots";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	
	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}
	
	@Override
	public void onInitialize() {
		RailbotsItemGroups.init();
		RailbotsItems.init();
		RailbotsBlocks.init();
		RailbotsBlockEntityTypes.init();
		RailbotsEntityTypes.init();
		RailbotsEvents.init();
		RailbotsTrackedData.init();
		RailbotsCommands.init();
		RailbotsEntityAttributes.init();
		RailbotsStatusEffects.init();
		RailbotsNetworking.init();
		RailbotsParticleTypes.init();
		RailbotsScreenHandlers.init();
		RailbotsStorages.init();
	}
}
