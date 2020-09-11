package net.ciebus.pinmod;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SMessageKeyPressedHandler implements IMessageHandler<MessageKeyPressed, IMessage> {

    @Override
    public IMessage onMessage(MessageKeyPressed message, MessageContext ctx) {
        PinRenderer.addPin(message.x, message.y, message.z, message.playerName, message.dimId);
        return null;
    }
}