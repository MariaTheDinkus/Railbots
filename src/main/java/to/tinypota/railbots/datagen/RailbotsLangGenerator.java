package to.tinypota.railbots.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import to.tinypota.railbots.registry.common.RailbotsBlocks;
import to.tinypota.railbots.registry.common.RailbotsItemGroups;

public class RailbotsLangGenerator extends FabricLanguageProvider {
	public RailbotsLangGenerator(FabricDataOutput output) {
		super(output);
	}
	
	@Override
	public void generateTranslations(TranslationBuilder translationBuilder) {
		translationBuilder.add(RailbotsItemGroups.RAILBOTS_KEY, "Railbots");
		
		translationBuilder.add(RailbotsBlocks.RAIL, "Rail");
	}
}