package to.tinypota.railbots.common.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import to.tinypota.railbots.common.storage.RailbotsRailState;
import to.tinypota.railbots.common.storage.RailbotsServerState;
import to.tinypota.railbots.registry.common.RailbotsStorages;

import java.util.Deque;

public class RailbotEntity extends PathAwareEntity {
	private int counter = 0;
	private Deque<BlockPos> path;
	
	public RailbotEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public boolean hasNoGravity() {
		return true;
	}
	
	@Override
	public boolean hasNoDrag() {
		return false;
	}
	
	@Override
	public boolean isCollidable() {
		return false;
	}
	
	@Override
	public boolean isPushable() {
		return false;
	}
	
	@Override
	public boolean collidesWith(Entity other) {
		return false;
	}
	
	@Override
	public boolean collidesWithStateAtPos(BlockPos pos, BlockState state) {
		return false;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!getWorld().isClient()) {
			if (path == null || path.isEmpty()) {
				var railState = (RailbotsRailState) RailbotsServerState.getSubState(getWorld().getServer(), RailbotsStorages.RAILBOTS_RAIL_STATE);
				var railGraph = railState.getRailGraph();
				getWorld().getEntitiesByType(EntityType.PLAYER, getBoundingBox().expand(64), player -> true).stream().findFirst().ifPresent(player -> {
					if (player.isSneaking()) {
						path = railGraph.findNearestPath(getBlockPos(), player.getBlockPos());
						if (!path.isEmpty())
							player.sendMessage(Text.literal("Railbot is on the move!"), false);
					}
				});
			} else if (getWorld().getTime() % 3 == 0 && !path.isEmpty()) {
				var railState = (RailbotsRailState) RailbotsServerState.getSubState(getWorld().getServer(), RailbotsStorages.RAILBOTS_RAIL_STATE);
				var railGraph = railState.getRailGraph();
				var node = path.pop();
				if (!railGraph.getGraph().containsKey(node)) {
					path = null;
				} else {
					teleport(node.getX() + 0.5, node.getY() + getHeight() / 2, node.getZ() + 0.5);
					((ServerWorld) getWorld()).spawnParticles(ParticleTypes.SMOKE, node.getX() + 0.5, node.getY() + 0.5, node.getZ() + 0.5, 20, 0, 0, 0, 0);
				}
			}
		}
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
	}
	
	public void setPath(Deque<BlockPos> path) {
		this.path = path;
	}
	
	public Deque<BlockPos> getPath() {
		return path;
	}
}
