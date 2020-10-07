package net.ciebus.pinmod.common.network;

import net.ciebus.pinmod.PinManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SMessageKeyPressedHandler implements IMessageHandler<MessageKeyPressed, IMessage> {

    @Override
    public IMessage onMessage(MessageKeyPressed message, MessageContext ctx) {
        PinManager.addPin(message.x,message.y,message.z,message.playerName,message.dimId);
        PacketHandler.INSTANCE1.sendToAll(message);
        return null;
    }

}
