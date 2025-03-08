package dev.oxydien.riamphur.mixin;

import dev.oxydien.riamphur.config.RiamphurConfig;
import dev.oxydien.riamphur.enchantments.Enchantments;
import dev.oxydien.riamphur.enchantments.SoulReaperEnchantment;
import dev.oxydien.riamphur.enums.SoulType;
import dev.oxydien.riamphur.interfaces.SoulAccessor;
import dev.oxydien.riamphur.items.Items;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements SoulAccessor {
	@Unique
	private SoulType riamphur$soulType = SoulType.HOSTILE; // Default to hostile <seealso cref="SoulAccessor"/>

	/**
	 * Getter for the soul type - implements interface method
	 */
	@Override
	public SoulType getRiamphurSoulType() {
		return riamphur$soulType;
	}

	/**
	 * Setter for the soul type - implements interface method
	 */
	@Override
	public void setRiamphurSoulType(SoulType type) {
		this.riamphur$soulType = type;
	}

	@Inject(method = "onDeath", at = @At("RETURN"))
	private void riamphur$onDeath(DamageSource source, CallbackInfo ci) {
		MobEntity thisEntity = (MobEntity) (Object) this;
		if (thisEntity.getWorld().isClient()) {
			return;
		}

		Entity attacker = source.getAttacker();

		// Check if the attacker is a player
		if (attacker instanceof ServerPlayerEntity playerAttacker) {
			// Get the item the player is holding in main hand
			ItemStack heldItem = playerAttacker.getMainHandStack();

			// Check if the item has the soul_harvester enchantment
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(heldItem);
			Enchantment soulHarvester = Enchantments.SOUL_REAPER;

			if (soulHarvester != null && enchantments.containsKey(soulHarvester)) {
				// Get the enchantment level
				int enchantmentLevel = enchantments.get(soulHarvester);

				// Drop the soul based on type and enchantment level
				riamphur$dropSoulForEntityType(thisEntity, playerAttacker, enchantmentLevel);
			}
			// If the held item is a soul extractor, force drop the soul
			else if (heldItem.getItem() == Items.SOUL_EXTRACTOR) {
				riamphur$dropSoulForEntityType(thisEntity, playerAttacker, 3, true);
			}
		}
	}

	/**
	 * Drops the appropriate soul item based on entity type and enchantment level
	 */
	@Unique
	private void riamphur$dropSoulForEntityType(MobEntity entity, ServerPlayerEntity player, int enchantmentLevel) {
		riamphur$dropSoulForEntityType(entity, player, enchantmentLevel, false);
	}

	@Unique
	private void riamphur$dropSoulForEntityType(MobEntity entity, ServerPlayerEntity player, int enchantmentLevel, boolean forceDrop) {
		// Get the soul type for this entity
		SoulType soulType = getRiamphurSoulType();

		String entityId = Registries.ENTITY_TYPE.getId(entity.getType()).toString();
		var configOverride = RiamphurConfig.INSTANCE.getSoulType(entityId);
		if (configOverride.isPresent()) {
			soulType = configOverride.get();
		}

		float dropChance = forceDrop
			? 1.0f
			: SoulReaperEnchantment.GetSoulDropChance(enchantmentLevel);

		// Random check for drop chance
		if (entity.getWorld().getRandom().nextFloat() > dropChance) {
			return;
		}

		// Get the appropriate soul item based on soul type
		Item soulItem = switch (soulType) {
			case PASSIVE -> Items.PASSIVE_SOUL;
			case HOSTILE -> Items.HOSTILE_SOUL;
			case BOSS_LIKE -> Items.BOSS_SOUL;
			case PLAYER -> Items.PLAYER_SOUL;
			default -> null;
		};

		// If the soul item exists, drop it
		if (soulItem != null) {
			ItemStack soulStack = new ItemStack(soulItem);

			entity.dropStack(soulStack);
		}
	}
}
