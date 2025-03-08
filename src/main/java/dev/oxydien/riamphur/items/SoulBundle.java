package dev.oxydien.riamphur.items;

import dev.oxydien.riamphur.enums.SoulType;
import dev.oxydien.riamphur.interfaces.EntitySoulAcceptor;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SoulBundle extends Item {
	public static final int MAX_PER_SOUL_TYPE = 1000; // Max number of souls per type
	private static final int ITEM_BAR_COLOR = MathHelper.color(0.4F, 0.4F, 1.0F);
	private static final String SOULS_NBT_KEY = "souls";

	private record SoulRemovalResult(int amount, Optional<Item> soulItem) {}

	public SoulBundle(Settings settings) {
		super(settings.maxCount(1));
	}

	public static boolean isSoulBundleFull(ItemStack stack) {
		if (stack.getItem() instanceof SoulBundle) {
			return getTotalSoulCount(stack) >= getMaxSouls();
		}
		return false;
	}

	public static int getMaxSouls() {
		return MAX_PER_SOUL_TYPE * 4;
	}

	public static int getAcceptableSoulCount(ItemStack stack, SoulType type, int amount) {
		if (stack.getItem() instanceof SoulBundle) {
			return Math.min(MAX_PER_SOUL_TYPE - getSoulCount(stack, type), amount);
		}
		return 0;
	}

	/**
	 * Get the current count of a specific soul type in the bundle
	 */
	private static int getSoulCount(ItemStack bundle, SoulType soulType) {
		NbtCompound nbt = bundle.getNbt();
		if (nbt == null || !nbt.contains(SOULS_NBT_KEY)) {
			return 0;
		}

		NbtCompound soulsNbt = nbt.getCompound(SOULS_NBT_KEY);
		return soulsNbt.contains(soulType.name().toLowerCase())
			? soulsNbt.getInt(soulType.name().toLowerCase())
			: 0;
	}

	/**
	 * Get the total number of souls in the bundle
	 */
	private static int getTotalSoulCount(ItemStack bundle) {
		NbtCompound nbt = bundle.getNbt();
		if (nbt == null || !nbt.contains(SOULS_NBT_KEY)) {
			return 0;
		}

		NbtCompound soulsNbt = nbt.getCompound(SOULS_NBT_KEY);
		int total = 0;
		for (SoulType type : SoulType.values()) {
			total += soulsNbt.contains(type.name().toLowerCase())
				? soulsNbt.getInt(type.name().toLowerCase())
				: 0;
		}
		return total;
	}

	/**
	 * Returns a float from 0 to 1 that represents how full the given bundle is.
	 * The fullness is determined by the total number of souls currently in the
	 * bundle divided by the maximum number of souls the bundle can hold.
	 * @param bundle the ItemStack to query the fullness of
	 * @return a float from 0 to 1 representing the fullness of the bundle
	 */
	private static float getBundleFullness(ItemStack bundle) {
		return getTotalSoulCount(bundle) / (float) getMaxSouls();
	}

	/**
	 * Add a soul to the bundle
	 * @return number of souls actually added
	 */
	public static int addSoulToBundle(ItemStack bundle, SoulItem soulItem, int targetAmount) {
		// Validate input
		if (soulItem == null) {
			return 0;
		}

		// Get or create NBT compound
		NbtCompound bundleNbt = bundle.getOrCreateNbt();
		NbtCompound soulsNbt = bundleNbt.contains(SOULS_NBT_KEY)
			? bundleNbt.getCompound(SOULS_NBT_KEY)
			: new NbtCompound();

		// Get soul type and current count
		SoulType soulType = soulItem.getSoulType();
		String soulTypeKey = soulType.name().toLowerCase();
		int currentCount = soulsNbt.contains(soulTypeKey)
			? soulsNbt.getInt(soulTypeKey)
			: 0;
		// Calculate available space
		int availableSpace = MAX_PER_SOUL_TYPE - currentCount;

		// Determine how many souls can be added
		int soulCountToAdd = Math.min(targetAmount, availableSpace);

		if (soulCountToAdd <= 0) {
			return 0; // Bundle is full
		}

		// Update the soul count
		soulsNbt.putInt(soulTypeKey, currentCount + soulCountToAdd);

		// Update bundle's NBT
		bundleNbt.put(SOULS_NBT_KEY, soulsNbt);
		bundle.setNbt(bundleNbt);

		return soulCountToAdd;
	}


	/**
	 * Remove a soul from the bundle
	 */
	private static SoulRemovalResult removeSoulFromBundle(ItemStack bundle, SoulType soulType, int amount) {
		NbtCompound bundleNbt = bundle.getNbt();
		if (bundleNbt == null || !bundleNbt.contains(SOULS_NBT_KEY)) {
			return new SoulRemovalResult(0, Optional.empty());
		}

		NbtCompound soulsNbt = bundleNbt.getCompound(SOULS_NBT_KEY);
		String soulTypeKey = soulType.name().toLowerCase();

		if (!soulsNbt.contains(soulTypeKey) || soulsNbt.getInt(soulTypeKey) <= 0) {
			return new SoulRemovalResult(0, Optional.empty());
		}

		// Reduce the soul count
		int currentCount = soulsNbt.getInt(soulTypeKey);
		int soulsToRemove = Math.min(amount, currentCount);
		soulsNbt.putInt(soulTypeKey, currentCount - soulsToRemove);

		// Update bundle's NBT
		bundleNbt.put(SOULS_NBT_KEY, soulsNbt);
		bundle.setNbt(bundleNbt);

		// Create and return the removed soul item
		return new SoulRemovalResult(soulsToRemove, createSoulItem(soulType));
	}

	/**
	 * Helper method to create a soul item of a specific type
	 */
	private static Optional<Item> createSoulItem(SoulType soulType) {
		return switch (soulType) {
			case PASSIVE -> Optional.of(Items.PASSIVE_SOUL);
			case HOSTILE -> Optional.of(Items.HOSTILE_SOUL);
			case BOSS_LIKE -> Optional.of(Items.BOSS_SOUL);
			case PLAYER -> Optional.of(Items.PLAYER_SOUL);
			default -> Optional.empty();
		};
	}

	@Override
	public boolean onClickedOnOther(ItemStack thisStack, Slot otherSlot, ClickType clickType, PlayerEntity player) {
		if (clickType == ClickType.RIGHT) {
			ItemStack itemStack = otherSlot.getStack();
			if (itemStack.isEmpty()) {
				SoulType typeToRemove = null;
				int maxSouls = 0;
				for (SoulType type : SoulType.values()) {
					int currentCount = getSoulCount(thisStack, type);
					if (currentCount > maxSouls) {
						maxSouls = currentCount;
						typeToRemove = type;
					}
				}
				if (typeToRemove != null) {
					SoulRemovalResult result = removeSoulFromBundle(thisStack, typeToRemove, SoulItem.DEFAULT_MAX_COUNT);
					if (result.soulItem.isPresent()) {
						player.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 1.0F, 1.0F);
						otherSlot.insertStack(new ItemStack(result.soulItem.get(), result.amount));
					}
				}
			}
			else if (itemStack.getItem() instanceof SoulItem soulItem) {
				int added = addSoulToBundle(thisStack, soulItem, itemStack.getCount());
				if (added > 0) {
					player.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 1.0F, 1.0F);
					itemStack.decrement(added);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onClicked(
		ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType,
		PlayerEntity player, StackReference cursorStackReference
	) {
		if (clickType == ClickType.RIGHT && thisSlot.canTakePartial(player)) {
			// If cursor is empty, try to remove a soul
			if (otherStack.isEmpty()) {
				for (SoulType type : SoulType.values()) {
					SoulRemovalResult result = removeSoulFromBundle(thisStack, type, 1);
					if (result.soulItem.isPresent()) {
						player.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 1.0F, 1.0F);
						cursorStackReference.set(new ItemStack(result.soulItem.get(), result.amount));
						break;
					}
				}
			}
			// If cursor has a soul item, try to add it
			else if (otherStack.getItem() instanceof SoulItem soulItem) {
				int added = addSoulToBundle(thisStack, soulItem, otherStack.getCount());
				if (added > 0) {
					player.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 1.0F, 1.0F);
					otherStack.decrement(added);
				}
			}

			return true;
		}

		return false;
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (user instanceof ServerPlayerEntity serverPlayerEntity) {
			if (entity instanceof EntitySoulAcceptor acceptor) {
				// For each soul type in the bundle, try to give it to the entity
				for (SoulType type : SoulType.values()) {
					int count = getSoulCount(stack, type);
					if (count > 0) {
						var amount = acceptor.CanAcceptSouls(type, serverPlayerEntity);
						if (amount > 0) {
							acceptor.AcceptSouls(type, Math.min(count, amount), serverPlayerEntity);
							removeSoulFromBundle(stack, type, amount);
						}
					}
				}
			}
		}
		return super.useOnEntity(stack, user, entity, hand);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		int totalSoulCount = getTotalSoulCount(stack);
		if (totalSoulCount > 0) {
			tooltip.add(Text.translatable("tooltip.riamphur.soul_bundle.total", totalSoulCount).styled(style -> style.withColor(0xC6C6C6)));
		}

		List<SoulType> sortedSoulTypes = Arrays.stream(SoulType.values())
			.sorted((a, b) -> Integer.compare(getSoulCount(stack, b), getSoulCount(stack, a)))
			.toList();

		for (SoulType type : sortedSoulTypes) {
			int count = getSoulCount(stack, type);
			if (count > 0) {
				Text text = switch (type) {
					case PASSIVE -> Text.translatable("tooltip.riamphur.soul_bundle.passive", count).styled(style -> style.withColor(0x32D2D9));
					case HOSTILE -> Text.translatable("tooltip.riamphur.soul_bundle.hostile", count).styled(style -> style.withColor(0xCA1D2C));
					case BOSS_LIKE -> Text.translatable("tooltip.riamphur.soul_bundle.boss", count).styled(style -> style.withColor(0x571D8D));
					case PLAYER -> Text.translatable("tooltip.riamphur.soul_bundle.player", count).styled(style -> style.withColor(0xC8C086));
					default -> Text.literal(type.name() + ": " + count);
				};
				tooltip.add(text);
			}
		}

		super.appendTooltip(stack, world, tooltip, context);
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return getTotalSoulCount(stack) > 0;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return Math.min(1 + (int) (12 * getBundleFullness(stack)), 13);
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		return ITEM_BAR_COLOR;
	}
}
