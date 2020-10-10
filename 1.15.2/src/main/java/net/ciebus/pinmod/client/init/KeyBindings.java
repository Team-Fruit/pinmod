package net.ciebus.pinmod.client.init;

import net.ciebus.pinmod.PinManager;
import net.ciebus.pinmod.client.render.PinRenderer;
import net.ciebus.pinmod.common.network.CMessageKeyPressedHandler;
import net.ciebus.pinmod.common.network.MessageKeyPressed;
import net.ciebus.pinmod.common.network.PacketHandler;
import net.ciebus.pinmod.common.network.SMessageKeyPressedHandler;
import net.java.games.input.Controller;
import net.java.games.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.PacketDistributor;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public final class KeyBindings {

    public static final KeyBinding sampleKey = new KeyBinding("Key.sample", GLFW_KEY_R, "CategoryName");

    public static void init() {
        ClientRegistry.registerKeyBinding(sampleKey);
        FMLJavaModLoadingContext.get().getModEventBus().register(new KeyBindings());
        MinecraftForge.EVENT_BUS.register(new KeyBindings());
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (sampleKey.isPressed()) {
            double maxdistance = 200d;
            PlayerEntity player = Minecraft.getInstance().player;
            Vec3d vec = player.getPositionVector();
            Vec3d vec3 = new Vec3d(vec.x, vec.y + player.getEyeHeight(), vec.z);
            Vec3d vec3a = player.getLook(1.0F);
            Vec3d vec3b = vec3.add(vec3a.getX() * maxdistance, vec3a.getY() * maxdistance, vec3a.getZ() * maxdistance);
            RayTraceResult mop = Minecraft.getInstance().world.rayTraceBlocks(new RayTraceContext(vec3, vec3b, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, player));
            if (mop != null) {
                boolean state = PinManager.isToDelete(mop.getHitVec().x, mop.getHitVec().y, mop.getHitVec().z, Minecraft.getInstance().player.getDisplayName().getFormattedText());
                PacketHandler.INSTANCE.sendToServer(new CMessageKeyPressedHandler(state, mop.getHitVec().x, mop.getHitVec().y, mop.getHitVec().z, Minecraft.getInstance().player.getDisplayName().getFormattedText(), player.world.getWorldType().getId()));
            }
        }
    }

}
