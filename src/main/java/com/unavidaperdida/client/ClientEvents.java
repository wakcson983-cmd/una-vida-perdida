package com.unavidaperdida.client;

import com.unavidaperdida.UnaVidaPerdida;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UnaVidaPerdida.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || ClientHandler.tick < 0) return;
        ClientHandler.tick++;
        if (ClientHandler.tick >= ClientHandler.TOTAL) ClientHandler.tick = -1;
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientHandler.tick = -1;
    }

    /** Sin pantalla abierta: se dibuja sobre el HUD. */
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (Minecraft.getInstance().screen != null) return;
        DeathOverlay.render(event.getGuiGraphics(), event.getPartialTick(),
                event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight());
    }

    /** Con pantalla abierta (por ejemplo la pantalla de muerte): se dibuja encima. */
    @SubscribeEvent
    public static void onRenderScreen(ScreenEvent.Render.Post event) {
        DeathOverlay.render(event.getGuiGraphics(), event.getPartialTick(),
                event.getScreen().width, event.getScreen().height);
    }
}
