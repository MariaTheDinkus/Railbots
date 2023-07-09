package to.tinypota.railbots.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.*;

import java.util.List;
import java.util.function.Consumer;

public class RailbotsRecipeGenerator extends FabricRecipeProvider {
	public RailbotsRecipeGenerator(FabricDataOutput output) {
		super(output);
	}
	
	@Override
	public void generate(Consumer<RecipeJsonProvider> exporter) {
//		ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, OllivandersBlocks.LATHE, 1).input('l', ItemTags.LOGS).input('i', Items.IRON_INGOT).pattern("lil").pattern("l l").pattern("lil").criterion("has_logs", VanillaRecipeProvider.conditionsFromTag(ItemTags.LOGS)).offerTo(exporter);
//		ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, OllivandersBlocks.VANISHING_CABINET, 1).input('l', Blocks.SPRUCE_LOG).input('d', Blocks.SPRUCE_DOOR).input('f', OllivandersItems.FLOO_POWDER).pattern(" f ").pattern("ldl").criterion("has_logs", VanillaRecipeProvider.conditionsFromTag(ItemTags.LOGS)).offerTo(exporter);
//		ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, OllivandersItems.CABINET_CORE, 1).input(Items.ENDER_PEARL).input(OllivandersItems.FLOO_POWDER, 4).criterion("has_ender_pearls", VanillaRecipeProvider.conditionsFromItem(Items.ENDER_PEARL)).offerTo(exporter);
	}
}