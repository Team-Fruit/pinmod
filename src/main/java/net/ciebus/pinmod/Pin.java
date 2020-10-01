package net.ciebus.pinmod;

import net.ciebus.pinmod.client.ClientProxy;
import net.ciebus.pinmod.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Pin.MODID)
public final class Pin {

    public static final String MODID = "pin";
    public static final String VERSION = "1.0";

    //@SidedProxy(clientSide = "net.ciebus.pinmod.client.ClientProxy", serverSide = "net.ciebus.pinmod.common.CommonProxy")
    public static CommonProxy commonProxy;
    public static ClientProxy clientProxy;

    private void initClient(final FMLClientSetupEvent event) {
        //クライアントサイド処理
        clientProxy.onPreInit();
        clientProxy.onInit();
    }

    private void initServer(final FMLCommonSetupEvent event) {
        commonProxy.onPreInit();
        commonProxy.onInit();
    }

    public Pin() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initServer);
    }
}
