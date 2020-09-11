package net.ciebus.pinmod;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class CMessageKeyPressedHandler implements IMessageHandler<MessageKeyPressed, IMessage> {

    @Override
    public IMessage onMessage(MessageKeyPressed message, MessageContext ctx) {
        // EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
        // entityPlayer.addChatComponentMessage(new ChatComponentText(String.format("%s pinned! %d: %f %f %f ", message.playerName, message.dimId,message.x,message.y,message.z)));
        //PinRenderer.addPin(message.x,message.y,message.z,message.playerName,message.dimId);
        PacketHandler.INSTANCE.sendToAll(message);
        //PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(mop.hitVec.xCoord,mop.hitVec.yCoord,mop.hitVec.zCoord, Minecraft.getMinecraft().thePlayer.worldObj.provider.dimensionId,M
        return null;
    }
}