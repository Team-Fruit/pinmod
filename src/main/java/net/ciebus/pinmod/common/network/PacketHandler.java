package net.ciebus.pinmod.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.ciebus.pinmod.common.network.CMessageKeyPressedHandler;
import net.ciebus.pinmod.common.network.MessageKeyPressed;
import net.ciebus.pinmod.common.network.SMessageKeyPressedHandler;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("pin");

    public static void init() {
        INSTANCE.registerMessage(CMessageKeyPressedHandler.class, MessageKeyPressed.class, 0, Side.SERVER);
        INSTANCE.registerMessage(SMessageKeyPressedHandler.class, MessageKeyPressed.class, 1, Side.CLIENT);
    }

}
