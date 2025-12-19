package net.vercte.satchels.platform.services;

import net.vercte.satchels.platform.Platform;

import java.nio.file.Path;

public interface IPlatformHelper {
    Platform getPlatform();
    Path getConfigDirectory();
}
