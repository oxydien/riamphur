package dev.oxydien.riamphur.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class SoulReaperEnchantment extends Enchantment {
	public static float GetSoulDropChance(int enchantmentLevel) {
		return 0.2f * enchantmentLevel;
	}

	protected SoulReaperEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
	public int getMinPower(int level) {
		return 12 + (level - 1) * 4;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean isTreasure() {
		return false;
	}

	@Override
	public boolean isAvailableForEnchantedBookOffer() {
		return true;
	}

	@Override
	public boolean isAvailableForRandomSelection() {
		return true;
	}
}
