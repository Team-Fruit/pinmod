package net.ciebus.pinmod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Pin.MODID, version = Pin.VERSION)
public class Pin {
    public static final String MODID = "pin";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "net.ciebus.pinmod.ClientProxy", serverSide = "net.ciebus.pinmod.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }


    @EventHandler
    public void load(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);
        FMLCommonHandler.instance().bus().register(new PinManager());
        MinecraftForge.EVENT_BUS.register(new PinManager());
        proxy.registerClientInfo();
    }


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.init();
    }


    @SubscribeEvent
    public void KeyHandlingEvent(InputEvent.KeyInputEvent event) {
        if (ClientProxy.sampleKey.isPressed()) {
            MovingObjectPosition mop = Minecraft.getMinecraft().renderViewEntity.rayTrace(200, 1.0F);
            if (mop != null) {
                boolean state = PinManager.isDelete(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, Minecraft.getMinecraft().thePlayer.getDisplayName(), Minecraft.getMinecraft().thePlayer.worldObj.provider.dimensionId);
                PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(state, mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, Minecraft.getMinecraft().thePlayer.getDisplayName(), Minecraft.getMinecraft().thePlayer.worldObj.provider.dimensionId));
            }
        }
    }
}
