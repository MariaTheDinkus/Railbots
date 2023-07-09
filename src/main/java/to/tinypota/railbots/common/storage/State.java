package to.tinypota.railbots.common.storage;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

public abstract class State<T extends PersistentState> {
	protected T serverState;
	
	public State() {
	
	}
	
	public State(T serverState) {
		this.serverState = serverState;
	}
	
	public <N extends State<T>> N withServerState(T serverState) {
		this.serverState = serverState;
		return (N) this;
	}
	
	public void setServerState(T serverState) {
		this.serverState = serverState;
	}
	
	public T getServerState() {
		return serverState;
	}
	
	public abstract NbtCompound writeToNbt(NbtCompound nbt);
	
	public abstract void fromNbt(T serverState, NbtCompound nbt);
	
	public void markDirty() {
		serverState.markDirty();
	}
}