package to.tinypota.railbots;

import net.fabricmc.api.ClientModInitializer;
import to.tinypota.railbots.registry.client.*;

public class RailbotsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		RailbotsNetworking.init();
		RailbotsEvents.init();
		RailbotsKeyBindings.init();
		RailbotsEntityRenderers.init();
		RailbotsBlockEntityRenderers.init();
		RailbotsBlockRenderLayers.init();
		RailbotsModels.init();
		
//		HandledScreens.<VanishingCabinetGuiDescription, VanishingCabinetScreen>register(RailbotsScreenHandlers.VANISHING_CABINET, (gui, inventory, title) -> new VanishingCabinetScreen(gui, inventory.player, title));
	}
}
