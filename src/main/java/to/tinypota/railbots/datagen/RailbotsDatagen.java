package to.tinypota.railbots.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.Nullable;
import to.tinypota.railbots.Railbots;
import to.tinypota.railbots.datagen.*;

public class RailbotsDatagen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		final var pack = dataGenerator.createPack();
		
		pack.addProvider(RailbotsLangGenerator::new);
		pack.addProvider(RailbotsModelGenerator::new);
		pack.addProvider(RailbotsBlockTagGenerator::new);
		pack.addProvider(RailbotsItemTagGenerator::new);
		pack.addProvider(RailbotsRecipeGenerator::new);
		pack.addProvider(RailbotsLootTableGenerator::new);
	}
	
	@Override
	public @Nullable String getEffectiveModId() {
		return Railbots.ID;
	}
}