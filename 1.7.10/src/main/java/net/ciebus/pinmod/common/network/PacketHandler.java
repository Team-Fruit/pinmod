package net.ciebus.pinmod.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.ciebus.pinmod.common.network.CMessageKeyPressedHandler;
import net.ciebus.pinmod.common.network.MessageKeyPressed;
import net.ciebus.pinmod.common.network.SMessageKeyPressedHandler;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE1 = NetworkRegistry.INSTANCE.newSimpleChannel("pin1");
    public static final SimpleNetworkWrapper INSTANCE2 = NetworkRegistry.INSTANCE.newSimpleChannel("pin2");

    public static void init() {
        INSTANCE1.registerMessage(CMessageKeyPressedHandler.class, MessageKeyPressed.class, 0, Side.CLIENT);
        INSTANCE2.registerMessage(SMessageKeyPressedHandler.class, MessageKeyPressed.class, 1, Side.SERVER);
    }

}
