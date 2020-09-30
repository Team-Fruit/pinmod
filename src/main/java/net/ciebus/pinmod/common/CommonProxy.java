package net.ciebus.pinmod.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.ciebus.pinmod.Pin;
import net.ciebus.pinmod.PinManager;
import net.ciebus.pinmod.common.data.PinData;
import net.ciebus.pinmod.common.network.PacketHandler;
import net.ciebus.pinmod.server.PinSynchronizer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    public void onPreInit(FMLPreInitializationEvent event) {
        PacketHandler.init();
    }

    public void onInit(FMLInitializationEvent event) {
        PinSynchronizer.init();
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

    }

    @SubscribeEvent
    public void onPlayerLeaved(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        for (PinData pin : PinManager.pins()) {
            PinManager.removePin(pin.player);
        }
    }
}
