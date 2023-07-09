package to.tinypota.railbots.registry.common;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class RailbotsNetworking {
//	public static final Identifier DECREASE_POWER_LEVEL = to.tinypota.railbots.Railbots.id("decrease_power_level");
//	public static final Identifier INCREASE_POWER_LEVEL = to.tinypota.railbots.Railbots.id("increase_power_level");
//
//	public static final Identifier SET_FLOO_FIRE_NAME = to.tinypota.railbots.Railbots.id("set_floor_fire_name");
//	public static final Identifier SWING_WAND_PACKET_ID = to.tinypota.railbots.Railbots.id("swing_wand");
//
//	public static final Identifier OPEN_FLOO_FIRE_NAME_SCREEN = to.tinypota.railbots.Railbots.id("open_floo_fire_name_screen");
//	public static final Identifier SYNC_POWER_LEVELS = to.tinypota.railbots.Railbots.id("sync_power_levels");
	
	public static void init() {
//		ServerPlayNetworking.registerGlobalReceiver(SYNC_POWER_LEVELS, (server, player, handler, buf, responseSender) -> {
//			server.execute(() -> {
//				var serverState = RailbotsServerState.getServerState(server);
//				serverState.syncPowerLevels(player);
//			});
//		});
	}
}