package net.ciebus.pinmod.common.network;


import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("mymodid", "main1"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static final SimpleChannel INSTANCE2 = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("mymodid", "main2"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    //public static final SimpleNetworkWrapper INSTANCE1 = NetworkRegistry.INSTANCE.newSimpleChannel("pin1");
    //public static final SimpleNetworkWrapper INSTANCE2 = NetworkRegistry.INSTANCE.newSimpleChannel("pin2");

    public static void init() {
        INSTANCE.registerMessage(0, CMessageKeyPressedHandler.class, CMessageKeyPressedHandler::toBytes, CMessageKeyPressedHandler::new, CMessageKeyPressedHandler::onMessage);
        INSTANCE2.registerMessage(1, SMessageKeyPressedHandler.class, SMessageKeyPressedHandler::toBytes, SMessageKeyPressedHandler::new, SMessageKeyPressedHandler::onMessage);
        //INSTANCE2.registerMessage(SMessageKeyPressedHandler.class, MessageKeyPressed.class, 1, Side.SERVER);
    }

}
