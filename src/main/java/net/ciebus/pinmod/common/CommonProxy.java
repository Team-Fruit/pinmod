package net.ciebus.pinmod.common;

import net.ciebus.pinmod.common.network.PacketHandler;
import net.ciebus.pinmod.server.PinSynchronizer;

public class CommonProxy {

    public void onPreInit() {
        PacketHandler.init();
    }

    public void onInit() {
        PinSynchronizer.init();
    }


}
