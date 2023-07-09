package to.tinypota.railbots.registry.common;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Rarity;
import to.tinypota.railbots.Railbots;

public class RailbotsItems {
//	public static final Item FLOO_POWDER = register("floo_powder", new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
	
	public static void init() {
	
	}
	
	public static <I extends Item> I register(String name, I item) {
		return register(name, item, RailbotsItemGroups.RAILBOTS_KEY);
	}
	
	public static <I extends Item> I register(String name, I item, RegistryKey<ItemGroup> group) {
		I result = Registry.register(Registries.ITEM, Railbots.id(name), item);
		if (group != null) {
			RailbotsItemGroups.addToItemGroup(group, item);
		}
		return result;
	}
}
