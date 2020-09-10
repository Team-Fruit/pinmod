package net.ciebus.pinmod;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy{
    public static final KeyBinding pinKey = new KeyBinding("pin", Keyboard.KEY_R, "pin");
    @Override
    public void registerClientInfo() {
        ClientRegistry.registerKeyBinding(pinKey);
    }
}
