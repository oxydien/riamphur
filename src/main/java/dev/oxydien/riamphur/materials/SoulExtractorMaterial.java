package dev.oxydien.riamphur.materials;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class SoulExtractorMaterial implements ToolMaterial {
	public static final SoulExtractorMaterial INSTANCE = new SoulExtractorMaterial();

	@Override
	public int getDurability() {
		return 12;
	}

	@Override
	public float getMiningSpeedMultiplier() {
		return 3.0f;
	}

	@Override
	public float getAttackDamage() {
		return 3.0f;
	}

	@Override
	public int getMiningLevel() {
		return 1;
	}

	@Override
	public int getEnchantability() {
		return 0;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return null;
	}
}
