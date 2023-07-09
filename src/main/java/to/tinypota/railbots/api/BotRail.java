package to.tinypota.railbots.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;

public interface BotRail {
  // todo expose bot interaction functionalities here!

  static boolean shouldConnect(WorldView world, BlockPos pos, BlockState state, Direction from) {
    var block = state.getBlock();
    if (!(block instanceof BotRail)) {
      return false;
    }

    return ((BotRail) block).canConnect(world, pos, state, from);
  }

  boolean canConnect(WorldView world, BlockPos pos, BlockState state, Direction from);

}