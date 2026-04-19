package com.niko.fullbright.mixin;

import com.niko.fullbright.FullbrightMod;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void fullbright$forceFull(LivingEntity entity, float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (FullbrightMod.isEnabled()) {
            cir.setReturnValue(1.0F);
        }
    }
}
