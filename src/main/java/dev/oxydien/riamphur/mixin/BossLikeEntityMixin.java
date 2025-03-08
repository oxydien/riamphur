package dev.oxydien.riamphur.mixin;

import dev.oxydien.riamphur.enums.SoulType;
import dev.oxydien.riamphur.interfaces.SoulAccessor;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.entity.mob.warden.WardenEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EnderDragonEntity.class, WitherEntity.class, WardenEntity.class, IronGolemEntity.class, PiglinBruteEntity.class})
public class BossLikeEntityMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void riamphurSetBossSoulType(CallbackInfo ci) {
		Object self = this;
		if (self instanceof SoulAccessor accessor) {
			accessor.setRiamphurSoulType(SoulType.BOSS_LIKE);
		}
	}
}
