package net.ciebus.pinmod.client;

import net.ciebus.pinmod.client.init.KeyBindings;
import net.ciebus.pinmod.client.init.Renderers;
import net.ciebus.pinmod.common.CommonProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void onInit(FMLInitializationEvent event) {
        super.onInit(event);
        Renderers.init();
        KeyBindings.init();
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
    }
}
