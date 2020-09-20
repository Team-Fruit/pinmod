package net.ciebus.pinmod.client.init;

import cpw.mods.fml.common.FMLCommonHandler;
import net.ciebus.pinmod.client.render.PinRenderer;
import net.minecraftforge.common.MinecraftForge;

public final class Renderers {

    private Renderers() {
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new PinRenderer());
        FMLCommonHandler.instance().bus().register(new PinRenderer());
    }

}
