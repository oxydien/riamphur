package dev.oxydien.riamphur.mixin;

import dev.oxydien.riamphur.Riamphur;
import dev.oxydien.riamphur.items.SoulBundle;
import dev.oxydien.riamphur.items.SoulItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

	@Shadow
	private int pickupDelay;

	@Inject(method = "onPlayerCollision", at = @At("HEAD"))
	private void riamphur$onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
		if (player instanceof ServerPlayerEntity serverPlayerEntity) {
			Object self = this;
			// Credit: boholder@github https://github.com/FabricMC/fabric/issues/1130#issuecomment-1598880692
			if (this.pickupDelay == 0 && self instanceof ItemEntity itemEntity) {
				ItemStack itemStack = itemEntity.getStack();
				Item item = itemStack.getItem();
				if (item instanceof SoulItem soulItem) {
					// Check if player has soul bundle in inventory
					for (ItemStack stack : serverPlayerEntity.getInventory().main) {
						if (stack.getItem() instanceof SoulBundle bundleItem) {
							// Add to bundle
							int added = SoulBundle.getAcceptableSoulCount(stack, soulItem.getSoulType(), itemStack.getCount());
							if (added > 0) {
								itemStack.decrement(added);
								Riamphur.Log("Added " + added + " " + soulItem.getSoulType() + " to " + bundleItem + " bundle");
								SoulBundle.addSoulToBundle(stack, soulItem, added);
							}
						}
					}
				}
			}
		}
	}
}
