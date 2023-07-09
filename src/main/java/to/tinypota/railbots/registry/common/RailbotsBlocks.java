package to.tinypota.railbots.registry.common;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import to.tinypota.railbots.Railbots;
import to.tinypota.railbots.common.block.RailbotRailBlock;

public class RailbotsBlocks {
	public static final RailbotRailBlock RAIL = register("rail", new RailbotRailBlock(FabricBlockSettings.copy(Blocks.ANVIL).pistonBehavior(PistonBehavior.NORMAL).nonOpaque()), new Item.Settings());
	
	public static void init() {
	
	}
	
	public static <B extends Block> B register(String name, B block) {
		return Registry.register(Registries.BLOCK, Railbots.id(name), block);
	}
	
	public static <B extends Block> B registerTallBlock(String name, B block, Item.Settings itemSettings) {
		var blockItem = new TallBlockItem(block, itemSettings);
		Registry.register(Registries.BLOCK, Railbots.id(name), block);
		Registry.register(Registries.ITEM, Railbots.id(name), blockItem);
		RailbotsItemGroups.addToDefault(blockItem);
		return block;
	}
	
	public static <B extends Block> B register(String name, B block, Item.Settings itemSettings) {
		var blockItem = new BlockItem(block, itemSettings);
		Registry.register(Registries.BLOCK, Railbots.id(name), block);
		Registry.register(Registries.ITEM, Railbots.id(name), blockItem);
		RailbotsItemGroups.addToDefault(blockItem);
		return block;
	}
}
