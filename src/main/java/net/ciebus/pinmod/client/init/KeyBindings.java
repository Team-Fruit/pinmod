package net.ciebus.pinmod.client.init;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.ciebus.pinmod.PinManager;
import net.ciebus.pinmod.common.network.MessageKeyPressed;
import net.ciebus.pinmod.common.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;

public final class KeyBindings {

    public static final KeyBinding sampleKey = new KeyBinding("Key.sample", Keyboard.KEY_R, "CategoryName");

    public static void init() {
        ClientRegistry.registerKeyBinding(sampleKey);
        FMLCommonHandler.instance().bus().register(new KeyBindings());
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (sampleKey.isPressed()) {
            MovingObjectPosition mop = Minecraft.getMinecraft().renderViewEntity.rayTrace(200, 1.0F);
            if (mop != null) {
                boolean state = PinManager.isDelete(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, Minecraft.getMinecraft().thePlayer.getDisplayName(), Minecraft.getMinecraft().thePlayer.worldObj.provider.dimensionId);
                PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(state, mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, Minecraft.getMinecraft().thePlayer.getDisplayName(), Minecraft.getMinecraft().thePlayer.worldObj.provider.dimensionId));
            }
        }
    }

}
