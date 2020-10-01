package net.ciebus.pinmod.client.init;

import net.ciebus.pinmod.PinManager;
import net.ciebus.pinmod.client.render.PinRenderer;
import net.ciebus.pinmod.common.network.MessageKeyPressed;
import net.ciebus.pinmod.common.network.PacketHandler;
import net.java.games.input.Controller;
import net.java.games.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public final class KeyBindings {

    public static final KeyBinding sampleKey = new KeyBinding("Key.sample", 1, "CategoryName");

    public static void init() {
        ClientRegistry.registerKeyBinding(sampleKey);
        FMLCommonHandler.instance().bus().register(new KeyBindings());
        MinecraftForge.EVENT_BUS.register(new KeyBindings());
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (sampleKey.isPressed()) {
            RayTraceResult mop = Minecraft.getInstance().renderViewEntity.rayTrace(200, 1.0F);
            if (mop != null) {
                boolean state = PinManager.isToDelete(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, Minecraft.getMinecraft().thePlayer.getDisplayName());
                PacketHandler.INSTANCE2.sendToServer(new MessageKeyPressed(state, mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, Minecraft.getMinecraft().thePlayer.getDisplayName(), Minecraft.getMinecraft().thePlayer.worldObj.provider.dimensionId));
            }
        }
    }

}
