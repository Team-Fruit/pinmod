package net.ciebus.pinmod;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SMessageKeyPressedHandler implements IMessageHandler<MessageKeyPressed, IMessage> {

    @Override
    public IMessage onMessage(MessageKeyPressed message, MessageContext ctx) {
        if(!message.state) {
            PinManager.addPin(message.x, message.y, message.z, message.playerName, message.dimId);
        } else {
            PinManager.removePin(message.playerName);
        }
        return null;
    }
}