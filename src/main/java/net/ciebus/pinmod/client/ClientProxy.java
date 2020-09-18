package net.ciebus.pinmod.client;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.ciebus.pinmod.client.init.KeyBindings;
import net.ciebus.pinmod.client.init.Renderers;
import net.ciebus.pinmod.common.CommonProxy;

public class ClientProxy extends CommonProxy {

    @Override
    public void onInit(FMLInitializationEvent event) {
        super.onInit(event);
        Renderers.init();
        KeyBindings.init();
    }

}
