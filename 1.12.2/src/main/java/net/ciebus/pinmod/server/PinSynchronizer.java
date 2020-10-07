package net.ciebus.pinmod.server;


import net.ciebus.pinmod.PinManager;
import net.ciebus.pinmod.client.init.KeyBindings;
import net.ciebus.pinmod.client.render.PinRenderer;
import net.ciebus.pinmod.common.data.PinData;
import net.ciebus.pinmod.common.network.MessageKeyPressed;
import net.ciebus.pinmod.common.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class PinSynchronizer {

    @SubscribeEvent
    //@SideOnly(Side.SERVER)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        //if (!event.player.worldObj.isRemote) {
            for (PinData pin : PinManager.pins()) {
                MessageKeyPressed pindata = new MessageKeyPressed(false, pin.x, pin.y, pin.z, pin.player, pin.dimId);
                PacketHandler.INSTANCE1.sendTo(pindata, (EntityPlayerMP) event.player);
            }
        //}
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        MessageKeyPressed pindata = new MessageKeyPressed(true,0,0,0,event.player.getDisplayName().getFormattedText(),0);
        PinManager.removePin(event.player.getDisplayName().getFormattedText());
        PacketHandler.INSTANCE1.sendToAll(pindata);
    }

    @SubscribeEvent
    public void onPlayerLeaved(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        for (PinData pin : PinManager.pins()) {
            PinManager.removePin(pin.player);
        }
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new PinSynchronizer());
        FMLCommonHandler.instance().bus().register(new PinSynchronizer());
    }
}
