package com.unavidaperdida.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unavidaperdida.UnaVidaPerdida;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Random;

public class DeathOverlay {
    private static final ResourceLocation SKULL =
            new ResourceLocation(UnaVidaPerdida.MOD_ID, "textures/gui/skull.png");
    private static final ResourceLocation TITLE =
            new ResourceLocation(UnaVidaPerdida.MOD_ID, "textures/gui/title.png");

    private static final int SKULL_W = 25, SKULL_H = 25;
    private static final int TITLE_W = 1024, TITLE_H = 112;

    public static void render(GuiGraphics g, float partial, int w, int h) {
        int tick = ClientHandler.tick;
        if (tick < 0) return;

        final int total = ClientHandler.TOTAL;
        float t = tick + partial;
        boolean self = ClientHandler.self;
        float f = self ? 1.0f : 0.5f; // el que murió lo ve el doble de grande

        Random r = new Random(tick * 7919L + 13L);

        // Intensidad del glitch: alta al inicio, baja en el medio, sube al final
        float gi;
        if (t < ClientHandler.GLITCH_IN) {
            gi = 1f - t / ClientHandler.GLITCH_IN;
        } else if (t >= ClientHandler.GLITCH_OUT_START) {
            gi = Math.min(1f, (t - ClientHandler.GLITCH_OUT_START) / (total - ClientHandler.GLITCH_OUT_START));
        } else {
            gi = r.nextFloat() < 0.05f ? 0.35f : 0f; // pequeños parpadeos ocasionales
        }

        float fade = Mth.clamp(Math.min(t / 6f, (total - t) / 8f), 0f, 1f);

        // Oscurecer un poco la pantalla
        int dim = (int) (fade * (self ? 110 : 50));
        if (dim > 0) g.fill(0, 0, w, h, dim << 24);

        // Parpadeo: a veces no se dibuja nada durante un frame
        if (gi > 0f && r.nextFloat() < gi * 0.3f) return;

        // Tamaños y posición
        int skullSize = Math.max(8, (int) (h * 0.40f * f));
        int titleW = (int) (Math.min(w * 0.60f, h * 1.6f) * f);
        int titleH = titleW * TITLE_H / TITLE_W;
        int gap = Math.max(2, (int) (h * 0.03f * f));
        int blockH = skullSize + gap + titleH;
        int y0 = (h - blockH) / 2;
        int skullX = (w - skullSize) / 2;
        int titleX = (w - titleW) / 2;
        int titleY = y0 + skullSize + gap;

        float alpha = 1f - (gi > 0f ? r.nextFloat() * 0.4f * gi : 0f);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        glitchBlit(g, SKULL, SKULL_W, SKULL_H, skullX, y0, skullSize, skullSize, gi, alpha, r);
        glitchBlit(g, TITLE, TITLE_W, TITLE_H, titleX, titleY, titleW, titleH, gi, alpha, r);

        // Barras de interferencia
        int bars = (int) (gi * 4);
        int minX = Math.min(skullX, titleX) - 6;
        int maxX = Math.max(skullX + skullSize, titleX + titleW) + 6;
        int[] colors = {0x66FF2040, 0x6620E0FF, 0x55FFFFFF};
        for (int i = 0; i < bars; i++) {
            int by = y0 + r.nextInt(Math.max(1, blockH));
            int bh = 1 + r.nextInt(3);
            g.fill(minX, by, maxX, by + bh, colors[r.nextInt(colors.length)]);
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    /**
     * Dibuja una textura por franjas horizontales. Con glitch > 0, cada franja se desplaza
     * hacia los lados y se dibujan copias rojas/cian desfasadas (aberración cromática).
     */
    private static void glitchBlit(GuiGraphics g, ResourceLocation tex, int texW, int texH,
                                   int x, int y, int w, int h, float gi, float alpha, Random r) {
        float scaleY = h / (float) texH;
        int maxBand = Math.max(2, texH / 6);
        int row = 0;
        while (row < texH) {
            int bandH = Math.min(1 + r.nextInt(maxBand), texH - row);
            int dy0 = y + Math.round(row * scaleY);
            int dy1 = y + Math.round((row + bandH) * scaleY);
            int dh = dy1 - dy0;

            int dx = 0;
            if (gi > 0f && r.nextFloat() < gi * 0.8f) {
                dx = (int) ((r.nextFloat() * 2f - 1f) * gi * w * 0.18f);
            }

            if (dh > 0) {
                if (gi > 0.05f) {
                    int ab = Math.max(1, (int) (gi * w * 0.03f));
                    RenderSystem.setShaderColor(1f, 0.1f, 0.2f, 0.55f * alpha);
                    g.blit(tex, x + dx - ab, dy0, w, dh, 0, row, texW, bandH, texW, texH);
                    RenderSystem.setShaderColor(0.1f, 1f, 1f, 0.55f * alpha);
                    g.blit(tex, x + dx + ab, dy0, w, dh, 0, row, texW, bandH, texW, texH);
                }
                RenderSystem.setShaderColor(1f, 1f, 1f, alpha);
                g.blit(tex, x + dx, dy0, w, dh, 0, row, texW, bandH, texW, texH);
            }
            row += bandH;
        }
    }
}
