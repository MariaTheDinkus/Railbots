package to.tinypota.railbots.common.storage;

import net.minecraft.nbt.NbtCompound;

public class RailbotsPlayerState extends State<RailbotsServerState> {
	public RailbotsPlayerState(RailbotsServerState serverState) {
		super(serverState);
	}
	
	/*
	 * Writes this OllivandersPlayerState to NBT.
	 */
	@Override
	public NbtCompound writeToNbt(NbtCompound nbt) {
		return nbt;
	}
	
	@Override
	public void fromNbt(RailbotsServerState serverState, NbtCompound nbt) {
	
	}
}