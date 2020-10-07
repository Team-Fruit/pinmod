package net.ciebus.pinmod.common.network;


import net.ciebus.pinmod.PinManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CMessageKeyPressedHandler implements IMessageHandler<MessageKeyPressed, IMessage> {

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
