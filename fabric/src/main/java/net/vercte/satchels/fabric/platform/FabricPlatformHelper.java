package net.vercte.satchels.fabric.platform;

import net.vercte.satchels.platform.Platform;
import net.vercte.satchels.platform.services.IPlatformHelper;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override public Platform getPlatform() { return Platform.FABRIC; }
}
