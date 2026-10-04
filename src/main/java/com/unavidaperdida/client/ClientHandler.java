package com.unavidaperdida.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

/** Estado de la animación en el cliente. */
public class ClientHandler {
    /** Duración total (ticks). 120 = 6 segundos. */
    public static final int TOTAL = 120;
    /** Ticks de glitch al inicio y momento en que empieza el glitch final. */
    public static final int GLITCH_IN = 14;
    public static final int GLITCH_OUT_START = 100;

    public static int tick = -1;       // -1 = sin animación
    public static boolean self = false;

    public static void start(boolean isSelf) {
        tick = 0;
        self = isSelf;
        // Sonido de cuando spawnea el Wither (más bajo para los demás jugadores)
        Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(SoundEvents.WITHER_SPAWN, 1.0f, isSelf ? 1.0f : 0.6f));
    }
}
