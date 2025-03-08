package dev.oxydien.riamphur.mixin;

import dev.oxydien.riamphur.enums.SoulType;
import dev.oxydien.riamphur.interfaces.SoulAccessor;
import net.minecraft.entity.passive.PassiveEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PassiveEntity.class)
public class PassiveEntityMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void riamphur$setPassiveSoulType(CallbackInfo ci) {
		// Use the object casting pattern with the interface
		Object self = this;
		if (self instanceof SoulAccessor accessor) {
			accessor.setRiamphurSoulType(SoulType.PASSIVE);
		}
	}
}
