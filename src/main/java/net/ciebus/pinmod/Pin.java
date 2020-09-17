package net.ciebus.pinmod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.ciebus.pinmod.common.CommonProxy;

@Mod(modid = Pin.MODID, version = Pin.VERSION)
public final class Pin {

    public static final String MODID = "pin";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "net.ciebus.pinmod.client.ClientProxy", serverSide = "net.ciebus.pinmod.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        proxy.onPreInit(event);
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
        proxy.onInit(event);
    }

}
