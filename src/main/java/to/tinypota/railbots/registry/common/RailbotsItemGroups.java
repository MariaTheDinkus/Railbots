package to.tinypota.railbots.registry.common;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import to.tinypota.railbots.Railbots;

public class RailbotsItemGroups {
	public static final RegistryKey<ItemGroup> RAILBOTS_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, Railbots.id("railbots_general"));
	public static final ItemGroup RAILBOTS = FabricItemGroup.builder().icon(() -> new ItemStack(RailbotsBlocks.RAIL)).entries((displayContext, entries) -> {}).displayName(Text.translatable("itemGroup.railbots.general")).build();
	
	public static void init() {
		register(RAILBOTS_KEY, RAILBOTS);
	}
	
	public static void addToItemGroup(RegistryKey<ItemGroup> group, Item item) {
		ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
	}
	
	public static void addToDefault(Item item) {
		ItemGroupEvents.modifyEntriesEvent(RAILBOTS_KEY).register(entries -> entries.add(item));
	}
	
	public static ItemGroup register(RegistryKey<ItemGroup> key, ItemGroup itemGroup) {
		return Registry.register(Registries.ITEM_GROUP, key, itemGroup);
	}
}
