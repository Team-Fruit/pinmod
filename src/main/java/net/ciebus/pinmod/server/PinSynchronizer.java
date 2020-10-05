package net.ciebus.pinmod.server;

import net.ciebus.pinmod.PinManager;
import net.ciebus.pinmod.common.data.PinData;
import net.ciebus.pinmod.common.network.MessageKeyPressed;
import net.ciebus.pinmod.common.network.PacketHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class PinSynchronizer {

    @SubscribeEvent
    //@SideOnly(Side.SERVER)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        //if (!event.player.worldObj.isRemote) {
            for (PinData pin : PinManager.pins()) {
                MessageKeyPressed pindata = new MessageKeyPressed(false, pin.x, pin.y, pin.z, pin.player, pin.dimId);
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with((Supplier<ServerPlayerEntity>) event.getPlayer()),pindata);
            }
        //}
    }

    //todo
    /*
    @SubscribeEvent
    public void onPlayerLeaved(ClientDisconnectionFromServerEvent event) {
        for (PinData pin : PinManager.pins()) {
            PinManager.removePin(pin.player);
        }
    }

     */

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new PinSynchronizer());
        FMLJavaModLoadingContext.get().getModEventBus().register(new PinSynchronizer());
    }
}
