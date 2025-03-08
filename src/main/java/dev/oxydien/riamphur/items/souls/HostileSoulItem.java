package dev.oxydien.riamphur.items.souls;

import dev.oxydien.riamphur.enums.SoulType;
import dev.oxydien.riamphur.items.SoulItem;

public class HostileSoulItem extends SoulItem {
	public HostileSoulItem(Settings settings) {
		super(settings);
	}

	@Override
	public SoulType getSoulType() {
		return SoulType.HOSTILE;
	}
}
