package dev.oxydien.riamphur.enchantments;

import dev.oxydien.riamphur.Riamphur;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Enchantments {
	public static final Enchantment SOUL_REAPER = registerEnchantment("soul_reaper", new SoulReaperEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] { EquipmentSlot.MAINHAND }));

	public static Enchantment registerEnchantment(String name, Enchantment enchantment) {
		return Registry.register(Registries.ENCHANTMENT, new Identifier(Riamphur.MOD_ID, name), enchantment);
	}

	public static void RegisterModEnchantments() {
		// Static loads this 'class' to register the enchantments above
		Riamphur.Log("Registering Enchantments!");
	}
}
