package net.vercte.satchels.compat.vanilla;

import net.neoforged.fml.ModList;
import net.vercte.satchels.compat.CompatEntrypoint;

public class VanillaCompat implements CompatEntrypoint {
    private static boolean isActive;

    @Override
    public void initialize() {

    }

    public static boolean shouldBeLoaded(ModList list) {
        return !list.isLoaded("curios");
    }
}
