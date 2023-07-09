package to.tinypota.railbots.common.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import to.tinypota.railbots.Railbots;
import to.tinypota.railbots.api.BotRail;
import to.tinypota.railbots.common.storage.RailbotsRailState;
import to.tinypota.railbots.common.storage.RailbotsServerState;
import to.tinypota.railbots.registry.common.RailbotsStorages;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class RailbotRailBlock extends Block implements BotRail {
	private static final Map<Direction, BooleanProperty> CONNECTIONS;
	private static final ArrayList<VoxelShape> SHAPES;
	
	public RailbotRailBlock(Settings settings) {
		super(settings);
		
		var defaultState = getDefaultState();
		for (var each : CONNECTIONS.values()) {
			defaultState = defaultState.with(each, false);
		}
		setDefaultState(defaultState);
	}
	
	private static void connectToAdjacentRails(World world, BlockPos pos, RailbotsRailState railState) {
		for (var direction : Direction.values()) {
			var neighborPos = pos.offset(direction);
			var neighborState = world.getBlockState(neighborPos);
			if (neighborState.getBlock() instanceof BotRail) {
				railState.addRail(pos, neighborPos);
			}
		}
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (!world.isClient()) {
			var railState = (RailbotsRailState) RailbotsServerState.getSubState(world.getServer(), RailbotsStorages.RAILBOTS_RAIL_STATE);
			connectToAdjacentRails(world, pos, railState);
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		if (!world.isClient() && newState.getBlock() != this) {
			Railbots.LOGGER.info("RailbotRailBlock.onStateReplaced: newState.getBlock() != this");
			var railState = (RailbotsRailState) RailbotsServerState.getSubState(world.getServer(), RailbotsStorages.RAILBOTS_RAIL_STATE);
			railState.removeRail(pos);
		}
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		var world = ctx.getWorld();
		var pos = ctx.getBlockPos();
		var neighborPos = new BlockPos.Mutable();
		var state = getDefaultState();
		for (Map.Entry<Direction, BooleanProperty> entry : CONNECTIONS.entrySet()) {
			var direction = entry.getKey();
			neighborPos.set(pos.offset(direction));
			var property = entry.getValue();
			state = state.with(property, BotRail.shouldConnect(world, neighborPos, world.getBlockState(neighborPos), direction.getOpposite()));
		}
		return state;
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return state.with(getConnectionProperty(direction), BotRail.shouldConnect(world, neighborPos, neighborState, direction.getOpposite()));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(RailbotRailBlock.CONNECTIONS.values().toArray(new Property<?>[0]));
	}
	
	@Override
	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		var shape = SHAPES.get(6);
		
		for (Direction each : CONNECTIONS.keySet()) {
			if (state.get(getConnectionProperty(each))) {
				shape = VoxelShapes.union(shape, SHAPES.get(each.getId()));
			}
		}
		
		return shape;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient() && player.getStackInHand(hand).isEmpty() && hand == Hand.MAIN_HAND) {
			var railState = (RailbotsRailState) RailbotsServerState.getSubState(world.getServer(), RailbotsStorages.RAILBOTS_RAIL_STATE);
			var railGraph = railState.getRailGraph();
			
			var path = railGraph.findNearestPath(new BlockPos(370, 65, 91), new BlockPos(372, 63, 102));
			
			while (!path.isEmpty()) {
				var node = path.pop();
				
				((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, node.getX() + 0.5, node.getY() + 0.5, node.getZ() + 0.5, 20, 0, 0, 0, 0);
			}
			
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}
	
	public static BooleanProperty getConnectionProperty(Direction direction) {
		return CONNECTIONS.get(direction);
	}
	
	static {
		CONNECTIONS = new EnumMap<>(Direction.class);
		for (var each : Direction.values()) {
			CONNECTIONS.put(each, BooleanProperty.of(each.getName()));
		}
		
		var thickness = 5.0;
		var begin = (8 - (thickness / 2)) / 16;
		var end = (8 + (thickness / 2)) / 16;
		SHAPES = new ArrayList<>(7);
		SHAPES.add(0, VoxelShapes.cuboid(begin, 0, begin, end, begin, end));
		SHAPES.add(1, VoxelShapes.cuboid(begin, end, begin, end, 1, end));
		SHAPES.add(2, VoxelShapes.cuboid(begin, begin, 0, end, end, begin));
		SHAPES.add(3, VoxelShapes.cuboid(begin, begin, end, end, end, 1));
		SHAPES.add(4, VoxelShapes.cuboid(0, begin, begin, begin, end, end));
		SHAPES.add(5, VoxelShapes.cuboid(end, begin, begin, 1, end, end));
		SHAPES.add(6, VoxelShapes.cuboid(begin, begin, begin, end, end, end));
	}
	
	@Override
	public boolean canConnect(WorldView world, BlockPos pos, BlockState state, Direction from) {
		return true;
	}
}
