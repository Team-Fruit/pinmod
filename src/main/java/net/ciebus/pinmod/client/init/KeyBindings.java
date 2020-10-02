package net.ciebus.pinmod.client.init;


import net.ciebus.pinmod.PinManager;
import net.ciebus.pinmod.client.render.PinRenderer;
import net.ciebus.pinmod.common.network.MessageKeyPressed;
import net.ciebus.pinmod.common.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

public final class KeyBindings {

    public static final KeyBinding sampleKey = new KeyBinding("Key.sample", Keyboard.KEY_R, "CategoryName");

    public static void init() {
        ClientRegistry.registerKeyBinding(sampleKey);
        FMLCommonHandler.instance().bus().register(new KeyBindings());
        MinecraftForge.EVENT_BUS.register(new KeyBindings());
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (sampleKey.isPressed()) {
            RayTraceResult mop = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(200, 1.0F);
            if (mop != null) {
                boolean state = PinManager.isToDelete(mop.hitVec.x, mop.hitVec.y, mop.hitVec.z, Minecraft.getMinecraft().player.getDisplayName().toString());
                PacketHandler.INSTANCE2.sendToServer(new MessageKeyPressed(state, mop.hitVec.x, mop.hitVec.y, mop.hitVec.z, Minecraft.getMinecraft().player.getDisplayName().getFormattedText(), Minecraft.getMinecraft().player.world.provider.getDimension()));
            }
        }
    }

}
