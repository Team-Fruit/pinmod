package net.ciebus.pinmod;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MessageKeyPressed implements IMessage {

    public boolean state;
    public double x;
    public double y;
    public double z;
    public int dimId;
    public String playerName;


    public MessageKeyPressed() {
    }

    public MessageKeyPressed(boolean state, double x, double y, double z, String playerName, int dimId) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimId = dimId;
        this.playerName = playerName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.state = buf.readBoolean();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.dimId = buf.readInt();
        this.playerName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(state);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeInt(dimId);
        ByteBufUtils.writeUTF8String(buf, playerName);
    }
}