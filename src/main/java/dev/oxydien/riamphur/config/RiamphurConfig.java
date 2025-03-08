package dev.oxydien.riamphur.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.oxydien.riamphur.Riamphur;
import dev.oxydien.riamphur.enums.SoulType;
import dev.oxydien.riamphur.utils.EnumUtils;
import org.quiltmc.loader.api.QuiltLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class RiamphurConfig {
	public static RiamphurConfig INSTANCE;
	private HashMap<String, SoulType> SoulTypeOverrides = null;
	private static final Path configPath = QuiltLoader.getConfigDir().resolve("riamphur.json");

	public static void LoadConfig() {
		Riamphur.Log("Loading config!");
		INSTANCE = new RiamphurConfig();
	}

	public RiamphurConfig() {
		this.loadConfig();
	}

	private void loadConfig() {
		if (!Files.exists(configPath)) {
			try {
				Files.createFile(configPath);
			} catch (IOException e) {
				Riamphur.Log("Failed to create config file!");
			}

			this.loadDefaultConfig();
			this.saveConfig();
			return;
		}

		List<String> lines = null;
		try {
			lines = Files.readAllLines(configPath);
		} catch (IOException e) {
			Riamphur.Log("Failed to read config file!");
		}
		assert lines != null;
		var content = String.join("\n", lines);

		JsonElement jsonElement = JsonParser.parseString(content);

		if (jsonElement.isJsonObject()) {
			var jsonObject = jsonElement.getAsJsonObject();

			if (jsonObject.has("soulTypeOverrides")) {
				var overrides = jsonObject.get("soulTypeOverrides").getAsJsonObject();
				this.SoulTypeOverrides = overrides
					.entrySet()
					.stream()
					.collect(HashMap::new,
						(map, entry) ->
							map.put(entry.getKey(), EnumUtils.soulTypeFromName(entry.getValue().getAsString())
							),
						HashMap::putAll
					);
			}
		}
	}

	private void saveConfig() {
		var gson = new GsonBuilder().setPrettyPrinting().create();
		var jsonElement = gson.toJsonTree(this.SoulTypeOverrides);
		var jsonString = gson.toJson(jsonElement);

		try {
			Files.write(configPath, jsonString.getBytes());
		} catch (IOException e) {
			Riamphur.Log("Failed to write config file!");
		}
	}

	private void loadDefaultConfig() {
		this.SoulTypeOverrides = new HashMap<>();

		// Some default overrides, most likely not all of them
		this.SoulTypeOverrides.put("minecraft:allay", SoulType.PASSIVE);
		this.SoulTypeOverrides.put("minecraft:cod", SoulType.PASSIVE);
		this.SoulTypeOverrides.put("minecraft:dolphin", SoulType.PASSIVE);
		this.SoulTypeOverrides.put("minecraft:axolotl", SoulType.PASSIVE);
		this.SoulTypeOverrides.put("minecraft:bat", SoulType.PASSIVE);
		this.SoulTypeOverrides.put("minecraft:salmon", SoulType.PASSIVE);
	}

	public Optional<SoulType> getSoulType(String id) {
		SoulType override = this.SoulTypeOverrides.get(id);
		if (override != null) {
			return Optional.of(override);
		}
		return Optional.empty();
	}
}
