package net.ciebus.pinmod.client.init;

import net.ciebus.pinmod.client.render.PinRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class Renderers {

    private Renderers() {
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new PinRenderer());
        FMLJavaModLoadingContext.get().getModEventBus().register(new PinRenderer());
    }

}
