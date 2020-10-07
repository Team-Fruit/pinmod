package net.ciebus.pinmod.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;

public class MessageKeyPressed {

    public boolean state;
    public double x;
    public double y;
    public double z;
    public int dimId;
    public int length;
    public String playerName;


    public MessageKeyPressed() {
    }

    public MessageKeyPressed(boolean state, double x, double y, double z, String playerName, int dimId) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimId = dimId;
        this.length = playerName.length();
        this.playerName = playerName;
    }

    public void fromBytes(PacketBuffer buf) {
        this.state = buf.readBoolean();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.dimId = buf.readInt();
        this.length = buf.readInt();
        this.playerName = buf.readString(length);
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBoolean(state);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeInt(dimId);
        buf.writeInt(length);
        buf.writeString(playerName);
    }
}
