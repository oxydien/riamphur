package dev.oxydien.riamphur.items;

import dev.oxydien.riamphur.enums.SoulType;
import dev.oxydien.riamphur.interfaces.SoulItemType;
import net.minecraft.item.Item;

public class SoulItem extends Item implements SoulItemType {
	public static final int DEFAULT_MAX_COUNT = 64;

	public SoulItem(Settings settings) {
		super(settings);
	}

	@Override
	public SoulType getSoulType() {
		return SoulType.NONE;
	}

}
