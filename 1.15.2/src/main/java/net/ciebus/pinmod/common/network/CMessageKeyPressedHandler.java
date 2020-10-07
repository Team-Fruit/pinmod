package net.ciebus.pinmod.common.network;

import net.ciebus.pinmod.PinManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CMessageKeyPressedHandler{
    public boolean state;
    public double x;
    public double y;
    public double z;
    public int dimId;
    public int length;
    public String playerName;


    public CMessageKeyPressedHandler() {
    }


    public CMessageKeyPressedHandler(boolean state, double x, double y, double z, String playerName, int dimId) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimId = dimId;
        this.length = playerName.length();
        this.playerName = playerName;
    }


    public CMessageKeyPressedHandler(PacketBuffer buf) {
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

    public void  onMessage(Supplier<NetworkEvent.Context> ctx) {
        if(!state) {
            PinManager.addPin(x, y, z, playerName, dimId);
        } else {
            PinManager.removePin(playerName);
        }
        // return null;
    }
}
