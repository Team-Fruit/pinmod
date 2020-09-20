package net.ciebus.pinmod.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.ciebus.pinmod.common.network.PacketHandler;
import net.ciebus.pinmod.server.PinSynchronizer;

public class CommonProxy {

    public void onPreInit(FMLPreInitializationEvent event) {
        PacketHandler.init();
    }

    public void onInit(FMLInitializationEvent event) {
       // PinSynchronizer.init();
    }

}
