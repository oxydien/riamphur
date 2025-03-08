package dev.oxydien.riamphur.items.souls;

import dev.oxydien.riamphur.enums.SoulType;
import dev.oxydien.riamphur.items.SoulItem;

public class BossSoulItem extends SoulItem {
	public BossSoulItem(Settings settings) {
		super(settings);
	}

	@Override
	public SoulType getSoulType() {
		return SoulType.BOSS_LIKE;
	}
}
