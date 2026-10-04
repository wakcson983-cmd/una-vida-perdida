package com.unavidaperdida.network;

import com.unavidaperdida.UnaVidaPerdida;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(UnaVidaPerdida.MOD_ID, "main"),
            () -> VERSION, VERSION::equals, VERSION::equals);

    public static void register() {
        CHANNEL.registerMessage(0, DeathAnimPacket.class,
                DeathAnimPacket::encode, DeathAnimPacket::decode, DeathAnimPacket::handle);
    }
}
