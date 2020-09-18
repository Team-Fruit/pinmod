package net.ciebus.pinmod;

import net.ciebus.pinmod.common.data.PinData;

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

    public static boolean isDelete(double x, double y, double z, String player, int dimId) {
        PinData pin = PINS.get(player);
        if (pin != null) {
            return Math.round(x) == Math.round(pin.x) && Math.round(y) == Math.round(pin.y) && Math.round(z) == Math.round(pin.z);
        }
        return false;
    }

    public static Collection<? extends PinData> pins() {
        return PINS_VIEW;
    }

}
