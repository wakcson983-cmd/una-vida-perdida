package com.unavidaperdida.network;

import com.unavidaperdida.client.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Se envía a cada jugador cuando alguien muere. self = true si ese jugador es el que murió. */
public class DeathAnimPacket {
    private final boolean self;

    public DeathAnimPacket(boolean self) {
        this.self = self;
    }

    public static void encode(DeathAnimPacket p, FriendlyByteBuf buf) {
        buf.writeBoolean(p.self);
    }

    public static DeathAnimPacket decode(FriendlyByteBuf buf) {
        return new DeathAnimPacket(buf.readBoolean());
    }

    public static void handle(DeathAnimPacket p, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.start(p.self)));
        ctx.get().setPacketHandled(true);
    }
}
