package to.tinypota.railbots.registry.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import to.tinypota.railbots.Railbots;

public class RailbotsNetworking {
	public static final Identifier SYNC_RAIL_GRAPH = Railbots.id("sync_rail_graph");
	
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC_RAIL_GRAPH, (client, handler, buf, responseSender) -> {
			var railGraphNbt = buf.readNbt();
			client.execute(() -> {
				RailbotsEvents.railGraph.fromNbt(railGraphNbt);
			});
		});
	}
}