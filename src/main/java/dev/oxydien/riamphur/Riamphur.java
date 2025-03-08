package dev.oxydien.riamphur;

import dev.oxydien.riamphur.config.RiamphurConfig;
import dev.oxydien.riamphur.enchantments.Enchantments;
import dev.oxydien.riamphur.items.Items;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Riamphur implements ModInitializer {
	public static final String MOD_ID = "riamphur";

	public static final Logger LOGGER = LoggerFactory.getLogger("Riamphur");

	@Override
	public void onInitialize(ModContainer mod) {
		Riamphur.Log("Initializing Riamphur! :3");
		RiamphurConfig.LoadConfig();
		Items.RegisterModItems();
		Enchantments.RegisterModEnchantments();
	}

	public static void Log(Object... messages) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("riamphur >: {}", String.join(" ", Arrays.stream(messages).map(Object::toString).toArray(String[]::new)));
		}
	}
}
