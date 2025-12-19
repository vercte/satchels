package net.vercte.satchels.neoforge.platform;

import net.neoforged.fml.loading.FMLPaths;
import net.vercte.satchels.platform.Platform;
import net.vercte.satchels.platform.services.IPlatformHelper;

import java.nio.file.Path;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override public Platform getPlatform() { return Platform.NEOFORGE; }

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
