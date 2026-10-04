package com.unavidaperdida.server;

import com.unavidaperdida.UnaVidaPerdida;
import com.unavidaperdida.network.DeathAnimPacket;
import com.unavidaperdida.network.ModNetwork;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = UnaVidaPerdida.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer dead)) return;
        MinecraftServer server = dead.getServer();
        if (server == null) return;

        for (ServerPlayer p : new ArrayList<>(server.getPlayerList().getPlayers())) {
            boolean self = p.getUUID().equals(dead.getUUID());
            ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), new DeathAnimPacket(self));
        }
    }
}
