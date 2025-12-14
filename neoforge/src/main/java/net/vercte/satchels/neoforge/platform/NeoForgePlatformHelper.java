package net.vercte.satchels.neoforge.platform;

import net.vercte.satchels.platform.Platform;
import net.vercte.satchels.platform.services.IPlatformHelper;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override public Platform getPlatform() { return Platform.NEOFORGE; }
}
