package dev.oxydien.riamphur.utils;

import dev.oxydien.riamphur.enums.SoulType;

import java.util.Arrays;

public class EnumUtils {
	public static SoulType soulTypeFromName(String name) {
		return Arrays.stream(SoulType.values())
			.filter(s -> s.name().equalsIgnoreCase(name))
			.findFirst()
			.orElse(SoulType.NONE);
	}

	public static String nameFromSoulType(SoulType soulType) {
		return soulType.name().toLowerCase();
	}
}
