package dev.oxydien.riamphur.items.souls;

import dev.oxydien.riamphur.enums.SoulType;
import dev.oxydien.riamphur.items.SoulItem;

public class PlayerSoulItem extends SoulItem {
	public PlayerSoulItem(Settings settings) {
		super(settings);
	}

	@Override
	public SoulType getSoulType() {
		return SoulType.PLAYER;
	}
}
