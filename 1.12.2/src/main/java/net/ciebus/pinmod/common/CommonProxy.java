package net.ciebus.pinmod.common;

import net.ciebus.pinmod.common.network.PacketHandler;
import net.ciebus.pinmod.server.PinSynchronizer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void onPreInit(FMLPreInitializationEvent event) {
        PacketHandler.init();
    }

    public void onInit(FMLInitializationEvent event) {
        PinSynchronizer.init();
    }


}
