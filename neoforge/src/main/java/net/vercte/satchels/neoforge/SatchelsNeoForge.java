package net.vercte.satchels.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.vercte.satchels.Satchels;
import net.neoforged.fml.common.Mod;
import net.vercte.satchels.neoforge.platform.NeoForgeRegistryHelper;

@Mod(Satchels.ID)
public final class SatchelsNeoForge {
    public SatchelsNeoForge(IEventBus bus) {
        Satchels.init();

        NeoForgeRegistryHelper.register(bus);
    }
}
