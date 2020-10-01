package net.ciebus.pinmod.client;

import net.ciebus.pinmod.client.init.KeyBindings;
import net.ciebus.pinmod.client.init.Renderers;
import net.ciebus.pinmod.common.CommonProxy;

public class ClientProxy extends CommonProxy {

    @Override
    public void onInit() {
        super.onInit();
        Renderers.init();
        KeyBindings.init();
    }

    @Override
    public void onPreInit() {
        super.onPreInit();
    }
}
