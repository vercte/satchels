package net.vercte.satchels.platform.services;

import net.minecraft.core.Registry;

import java.util.function.Supplier;

public interface IRegistryHelper {
    <V, T extends V> Supplier<T> register(Registry<V> registry, String path, Supplier<T> object);
}
