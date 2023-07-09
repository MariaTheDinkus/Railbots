package to.tinypota.railbots.common.storage;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import to.tinypota.railbots.api.RailGraph;
import to.tinypota.railbots.registry.client.RailbotsNetworking;

import java.util.Collection;

public class RailbotsRailState extends State<RailbotsServerState> {
	private RailGraph railGraph = new RailGraph();
	
	public RailbotsRailState() {
	
	}
	
	public RailbotsRailState(RailbotsServerState serverState) {
		super(serverState);
	}
	
	public RailGraph getRailGraph() {
		return railGraph;
	}
	
	public void addRail(BlockPos source, BlockPos target) {
		railGraph.addRail(source, target);
		markDirty();
	}
	
	public void removeRail(BlockPos rail) {
		railGraph.removeRail(rail);
		markDirty();
	}
	
	public Collection<BlockPos> getConnectedRails(BlockPos rail) {
		return railGraph.getConnectedRails(rail);
	}
	
	@Override
	public NbtCompound writeToNbt(NbtCompound nbt) {
		nbt.put("railGraph", railGraph.toNbt(new NbtCompound()));
		return nbt;
	}
	
	@Override
	public void fromNbt(RailbotsServerState serverState, NbtCompound nbt) {
		railGraph.fromNbt(nbt.getCompound("railGraph"));
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
		for (var player : serverState.getServer().getPlayerManager().getPlayerList()) {
			var buf = PacketByteBufs.create();
			buf.writeNbt(railGraph.toNbt(new NbtCompound()));
			ServerPlayNetworking.send(player, RailbotsNetworking.SYNC_RAIL_GRAPH, buf);
		}
	}
}