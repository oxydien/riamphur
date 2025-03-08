package dev.oxydien.riamphur.items;

import dev.oxydien.riamphur.Riamphur;
import dev.oxydien.riamphur.items.souls.BossSoulItem;
import dev.oxydien.riamphur.items.souls.HostileSoulItem;
import dev.oxydien.riamphur.items.souls.PassiveSoulItem;
import dev.oxydien.riamphur.items.souls.PlayerSoulItem;
import dev.oxydien.riamphur.materials.SoulExtractorMaterial;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.quiltmc.qsl.item.setting.api.QuiltCustomItemSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class Items {
	public static final String MISCHIEF_ITEM_GROUP = "riamphur-mischief";

	// [GROUPS]

	public static final ItemGroup ModItemsGroup = FabricItemGroup.builder()
		.name(Text.translatable("itemgroup.riamphur.riamphur-mischief"))
		.icon(() -> new ItemStack(net.minecraft.item.Items.GHAST_TEAR)).build();


	// [INGREDIENTS]
	public static final Item PASSIVE_SOUL = registerItem("passive_soul", new PassiveSoulItem(new QuiltItemSettings().rarity(Rarity.UNCOMMON)));
	public static final Item HOSTILE_SOUL = registerItem("hostile_soul", new HostileSoulItem(new QuiltItemSettings().rarity(Rarity.UNCOMMON)));
	public static final Item BOSS_SOUL = registerItem("boss_soul", new BossSoulItem(new QuiltItemSettings().rarity(Rarity.UNCOMMON)));
	public static final Item PLAYER_SOUL = registerItem("player_soul", new PlayerSoulItem(new QuiltItemSettings().rarity(Rarity.UNCOMMON)));

	// [TOOLS]
	public static final Item SOUL_EXTRACTOR = registerItem("soul_extractor", new SoulExtractorItem(SoulExtractorMaterial.INSTANCE, new QuiltItemSettings().maxDamage(12)));
	public static final Item SOUL_BUNDLE = registerItem("soul_bundle", new SoulBundle(new QuiltItemSettings()));



	private static Item registerItem(String name, Item item) {
		Item NITEM = Registry.register(Registries.ITEM, new Identifier(Riamphur.MOD_ID, name), item);
		AddItemToGroup(NITEM);
		return NITEM;
	}

	public static void AddItemToGroup(Item item) {
		ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(RegistryKeys.ITEM_GROUP,
				new Identifier(Riamphur.MOD_ID, MISCHIEF_ITEM_GROUP)))
			.register(entries -> {
				entries.addItem(item);
			});
	}

	public static void RegisterModItems() {
		// Static loads this 'class' to register the items at the top of this file
		Riamphur.Log("Registering Items!");

		Registry.register(Registries.ITEM_GROUP, new Identifier(Riamphur.MOD_ID, MISCHIEF_ITEM_GROUP), ModItemsGroup);
	}
}
