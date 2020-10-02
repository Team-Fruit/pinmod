package net.ciebus.pinmod.common.network;


import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE1 = NetworkRegistry.INSTANCE.newSimpleChannel("pin1");
    public static final SimpleNetworkWrapper INSTANCE2 = NetworkRegistry.INSTANCE.newSimpleChannel("pin2");

    public static void init() {
        INSTANCE1.registerMessage(CMessageKeyPressedHandler.class, MessageKeyPressed.class, 0, Side.CLIENT);
        INSTANCE2.registerMessage(SMessageKeyPressedHandler.class, MessageKeyPressed.class, 1, Side.SERVER);
    }

}
