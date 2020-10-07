package net.ciebus.pinmod;

import net.ciebus.pinmod.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Pin.MODID, version = Pin.VERSION)
public final class Pin {

    public static final String MODID = "pin";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "net.ciebus.pinmod.client.ClientProxy", serverSide = "net.ciebus.pinmod.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        proxy.onPreInit(event);
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        proxy.onInit(event);
    }

}
