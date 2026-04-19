package com.niko.fullbright;

import com.mojang.blaze3d.platform.InputConstants;
import com.niko.fullbright.mixin.OptionInstanceAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;

@Mod(value = FullbrightMod.MOD_ID, dist = Dist.CLIENT)
public final class FullbrightMod {
    public static final String MOD_ID = "fullbright";
    private static final double BOOSTED_GAMMA = 16.0;

    private static boolean enabled = false;
    private static Double savedGamma = null;

    private static final KeyMapping TOGGLE_KEY = new KeyMapping(
        "key.fullbright.toggle",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_G,
        KeyMapping.Category.MISC
    );

    public FullbrightMod(IEventBus modBus) {
        modBus.addListener(FullbrightMod::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.addListener(FullbrightMod::onClientTick);
    }

    public static boolean isEnabled() {
        return enabled;
    }

    private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_KEY);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        while (TOGGLE_KEY.consumeClick()) {
            enabled = !enabled;
            applyGamma(mc);
            if (mc.player != null) {
                mc.player.sendOverlayMessage(
                    Component.translatable(enabled
                        ? "text.fullbright.enabled"
                        : "text.fullbright.disabled")
                );
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void applyGamma(Minecraft mc) {
        if (mc.options == null) return;
        OptionInstance<Double> gamma = mc.options.gamma();
        OptionInstanceAccessor<Double> accessor = (OptionInstanceAccessor<Double>) (Object) gamma;

        if (enabled) {
            if (savedGamma == null) {
                savedGamma = gamma.get();
            }
            accessor.fullbright$setValue(BOOSTED_GAMMA);
        } else if (savedGamma != null) {
            accessor.fullbright$setValue(savedGamma);
            savedGamma = null;
        }
    }
}
