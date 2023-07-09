package to.tinypota.railbots.registry.common;

import net.minecraft.util.Identifier;
import to.tinypota.railbots.Railbots;
import to.tinypota.railbots.common.storage.RailbotsRailState;
import to.tinypota.railbots.common.storage.RailbotsServerState;

public class RailbotsStorages {
	public static Identifier RAILBOTS_RAIL_STATE = RailbotsServerState.addSubState(Railbots.id("rail"), new RailbotsRailState());
	
	public static void init() {
	
	}
}
