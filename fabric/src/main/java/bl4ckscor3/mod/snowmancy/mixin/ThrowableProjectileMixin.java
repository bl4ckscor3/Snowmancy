package bl4ckscor3.mod.snowmancy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import bl4ckscor3.mod.snowmancy.Snowmancy;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;

@Mixin(ThrowableProjectile.class)
public class ThrowableProjectileMixin {
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/HitResult;getLocation()Lnet/minecraft/world/phys/Vec3;"))
	private void tellSnowmanBuilderAboutImpact(CallbackInfo ci, @Local HitResult result) {
		Snowmancy.onProjectileImpactThrowable((ThrowableProjectile) (Object) this, result);
	}
}
