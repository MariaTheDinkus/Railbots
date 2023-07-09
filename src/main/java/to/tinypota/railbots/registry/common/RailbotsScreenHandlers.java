package to.tinypota.railbots.registry.common;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import to.tinypota.railbots.Railbots;

public class RailbotsScreenHandlers {
//	public static final ScreenHandlerType<VanishingCabinetGuiDescription> VANISHING_CABINET = register("vanishing_cabinet", new ScreenHandlerType<>((syncId, playerInventory) -> new VanishingCabinetGuiDescription(syncId, playerInventory, ScreenHandlerContext.EMPTY), FeatureFlags.VANILLA_FEATURES));
	
	public static void init() {
	
	}
	
	public static <H extends ScreenHandlerType<?>> H register(String name, H handler) {
		H result = Registry.register(Registries.SCREEN_HANDLER, Railbots.id(name), handler);
		return result;
	}
}
