package net.ciebus.pinmod.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.ciebus.pinmod.PinManager;

public class SMessageKeyPressedHandler implements IMessageHandler<MessageKeyPressed, IMessage> {

    @Override
    public IMessage onMessage(MessageKeyPressed message, MessageContext ctx) {
        PinManager.addPin(message.x,message.y,message.z,message.playerName,message.dimId);
        PacketHandler.INSTANCE1.sendToAll(message);
        return null;
    }

}
