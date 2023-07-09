package to.tinypota.railbots.common.storage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import to.tinypota.railbots.Railbots;

import java.util.HashMap;
import java.util.UUID;

public class RailbotsServerState extends PersistentState {
	private final HashMap<UUID, RailbotsPlayerState> players = new HashMap<>();
	private static final HashMap<Identifier, State<RailbotsServerState>> SUB_STATES = new HashMap<>();
	private static MinecraftServer server;
	
	public static Identifier addSubState(Identifier identifier, State<RailbotsServerState> state) {
		SUB_STATES.put(identifier, state);
		
		return identifier;
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		var playersNbtCompound = new NbtCompound();
		players.forEach((uuid, playerState) -> {
			var stateNbt = playerState.writeToNbt(new NbtCompound());
			playersNbtCompound.put(String.valueOf(uuid), stateNbt);
		});
		nbt.put("players", playersNbtCompound);
		
		for (var entry : SUB_STATES.entrySet()) {
			var identifier = entry.getKey();
			var state = entry.getValue();
			var stateNbt = state.writeToNbt(new NbtCompound());
			nbt.put(identifier.toString(), stateNbt);
		}
		
		return nbt;
	}
	
	public static RailbotsServerState readNbt(NbtCompound tag) {
		var serverState = new RailbotsServerState();
		var playersTag = tag.getCompound("players");
		playersTag.getKeys().forEach(key -> {
			var playerTag = playersTag.getCompound(key);
			var playerState = new RailbotsPlayerState(serverState);
			playerState.fromNbt(serverState, playerTag);
			var uuid = UUID.fromString(key);
			serverState.players.put(uuid, playerState);
		});
		
		for (var entry : SUB_STATES.entrySet()) {
			var identifier = entry.getKey();
			var state = entry.getValue();
			var stateNbt = tag.getCompound(identifier.toString());
			state.fromNbt(serverState, stateNbt);
		}
		
		return serverState;
	}
	
	public static RailbotsServerState getServerState(MinecraftServer server) {
		var persistentStateManager = server.getOverworld().getPersistentStateManager();
		RailbotsServerState.server = server;
		return persistentStateManager.getOrCreate(RailbotsServerState::readNbt, RailbotsServerState::new, Railbots.ID);
	}
	
	public static RailbotsPlayerState getSubState(MinecraftServer server, PlayerEntity player) {
		var serverState = getServerState(server);
		return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new RailbotsPlayerState(serverState));
	}
	
	public RailbotsPlayerState getSubState(PlayerEntity player) {
		return players.computeIfAbsent(player.getUuid(), uuid -> new RailbotsPlayerState(this));
	}
	
	public static <T extends State<RailbotsServerState>> T getSubState(MinecraftServer server, Identifier identifier) {
		var serverState = getServerState(server);
		return serverState.getSubState(identifier);
	}
	
	public <T extends State<RailbotsServerState>> T getSubState(Identifier identifier) {
		return SUB_STATES.get(identifier).withServerState(this);
	}
	
	public MinecraftServer getServer() {
		return RailbotsServerState.server;
	}
}