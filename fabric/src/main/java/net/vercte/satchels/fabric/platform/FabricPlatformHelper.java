package net.vercte.satchels.fabric.platform;

import net.fabricmc.loader.api.FabricLoader;
import net.vercte.satchels.platform.Platform;
import net.vercte.satchels.platform.services.IPlatformHelper;

import java.nio.file.Path;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override public Platform getPlatform() { return Platform.FABRIC; }

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
