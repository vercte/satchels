package net.vercte.satchels.fabric.platform;

import net.minecraft.core.Registry;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.platform.services.IRegistryHelper;

import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public <V, T extends V> Supplier<T> register(Registry<V> registry, String path, Supplier<T> object) {
        T result = Registry.register(registry, Satchels.at(path), object.get());
        return () -> result;
    }
}
