package to.tinypota.railbots.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import to.tinypota.railbots.registry.common.RailbotsBlocks;

public class RailbotsLootTableGenerator extends FabricBlockLootTableProvider {
	public RailbotsLootTableGenerator(FabricDataOutput output) {
		super(output);
	}
	
	@Override
	public void generate() {
		addDrop(RailbotsBlocks.RAIL);
	}
}