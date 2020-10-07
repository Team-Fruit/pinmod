package net.ciebus.pinmod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.ciebus.pinmod.common.data.PinData;
import net.ciebus.pinmod.server.PinSynchronizer;
import net.minecraftforge.common.MinecraftForge;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PinManager {

    // TODO: Load from database
    private static final Map<String, PinData> PINS = new HashMap<String, PinData>();

    // HashMap#values is unmodifiable. But that Iterator supports remove operation.
    private static final Collection<PinData> PINS_VIEW = Collections.unmodifiableCollection(PINS.values());

    public static void addPin(double x, double y, double z, String player, int dimId) {
        PINS.put(player, new PinData(x, y, z, player, dimId));
    }

    public static void removePin(String player) {
        PINS.remove(player);
    }

    public static boolean isToDelete(double hitX, double hitY, double hitZ, String player) {
        PinData pin = PINS.get(player);
        if (pin != null) {
            return Math.round(hitX) == Math.round(pin.x) && Math.round(hitY) == Math.round(pin.y) && Math.round(hitZ) == Math.round(pin.z);
        }
        return false;
    }

    public static Collection<? extends PinData> pins() {
        return PINS_VIEW;
    }


}
