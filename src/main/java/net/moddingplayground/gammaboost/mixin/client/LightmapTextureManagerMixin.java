package net.moddingplayground.gammaboost.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.moddingplayground.gammaboost.api.client.config.GammaBoostConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {
    @Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
    private static void applyBrightness(DimensionType type, int lightLevel, CallbackInfoReturnable<Float> cir) {
        GammaBoostConfig config = GammaBoostConfig.INSTANCE;
        if (config.enabled.is(false) || config.underground_enabled.is(false) || lightLevel < config.underground_minimumLightLevel.getValue()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world.getRegistryKey() != World.OVERWORLD) return;

        // boost ambient light underground
        double y = client.player.getY();
        int startY = config.underground_startY.getValue();
        if (y <= startY) {
            int endY = config.underground_endY.getValue();
            float base = config.underground_base.getValue() / 10000f;
            float original = cir.getReturnValueF();
            if (y >= endY) {
                // original + base(cos((y - endY) / (abs(startY - endY) / PI) + 1) / 2)
                cir.setReturnValue(original + (base * (float) ((Math.cos((y - endY) / (Math.abs(startY - endY) / Math.PI)) + 1) / 2)));
            } else cir.setReturnValue(original + base);
        }
    }
}
