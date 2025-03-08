package dev.oxydien.riamphur.interfaces;

import dev.oxydien.riamphur.enums.SoulType;
import net.minecraft.server.network.ServerPlayerEntity;

public interface EntitySoulAcceptor {

	/**
	 * @param type The type of soul to check for acceptance
	 * @param player The player that is trying to accept the souls
	 * @return The amount of souls that the entity can accept
	 */
	public int CanAcceptSouls(SoulType type, ServerPlayerEntity player);

	/**
	 * @param type The type of soul to accept
	 * @param amount The amount of the specified type of soul to accept
	 * @param player The player that is accepting the souls
	 */
	public void AcceptSouls(SoulType type, int amount, ServerPlayerEntity player);
}
