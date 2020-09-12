package net.ciebus.pinmod;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class SMessageKeyPressedHandler implements IMessageHandler<MessageKeyPressed, IMessage> {

    @Override
    public IMessage onMessage(MessageKeyPressed message, MessageContext ctx) {
        PacketHandler.INSTANCE.sendToAll(message);
        return null;
    }
}