package com.unavidaperdida;

import com.unavidaperdida.network.ModNetwork;
import net.minecraftforge.fml.common.Mod;

@Mod(UnaVidaPerdida.MOD_ID)
public class UnaVidaPerdida {
    public static final String MOD_ID = "una_vida_perdida";

    public UnaVidaPerdida() {
        ModNetwork.register();
    }
}
