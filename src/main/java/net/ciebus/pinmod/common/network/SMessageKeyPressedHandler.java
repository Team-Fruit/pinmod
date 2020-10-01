package net.ciebus.pinmod.common.network;

import net.ciebus.pinmod.PinManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class SMessageKeyPressedHandler{

    public boolean state;
    public double x;
    public double y;
    public double z;
    public int dimId;
    public int length;
    public String playerName;


    public SMessageKeyPressedHandler() {
    }


    public SMessageKeyPressedHandler(boolean state, double x, double y, double z, String playerName, int dimId) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimId = dimId;
        this.length = playerName.length();
        this.playerName = playerName;
    }


    public SMessageKeyPressedHandler(PacketBuffer buf) {
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

    public void onMessage(Supplier<NetworkEvent.Context> ctx) {
        PinManager.addPin(x, y, z, playerName, dimId);
        PacketHandler.INSTANCE2.send(PacketDistributor.ALL.noArg(),this);
        //return null;
    }

}
