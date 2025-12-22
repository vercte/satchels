package net.vercte.satchels.platform;

import com.mojang.logging.LogUtils;
import net.vercte.satchels.platform.services.INetworkHelper;
import net.vercte.satchels.platform.services.IPlatformHelper;
import net.vercte.satchels.platform.services.IRegistryHelper;
import net.vercte.satchels.platform.services.ISlotModHelper;

import java.util.ServiceLoader;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);
    public static final INetworkHelper NETWORK = load(INetworkHelper.class);
    public static final ISlotModHelper SLOT_MOD = load(ISlotModHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LogUtils.getLogger().debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
