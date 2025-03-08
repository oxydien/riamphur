package dev.oxydien.riamphur.items.souls;

import dev.oxydien.riamphur.enums.SoulType;
import dev.oxydien.riamphur.items.SoulItem;

public class PassiveSoulItem extends SoulItem {
	public PassiveSoulItem(Settings settings) {
		super(settings);
	}

	@Override
	public SoulType getSoulType() {
		return SoulType.PASSIVE;
	}
}
