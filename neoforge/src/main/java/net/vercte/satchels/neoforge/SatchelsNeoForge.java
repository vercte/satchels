package net.vercte.satchels.neoforge;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.vercte.satchels.Satchels;
import net.neoforged.fml.common.Mod;
import net.vercte.satchels.neoforge.platform.NeoForgeRegistryHelper;
import net.vercte.satchels.network.ToggleSatchelPacket;

@Mod(Satchels.ID)
public final class SatchelsNeoForge {
    public SatchelsNeoForge(IEventBus bus) {
        Satchels.init();

        bus.addListener(SatchelsNeoForge::registerBindings);
        bus.addListener(SatchelsNeoForge::registerPayloadHandlers);
        NeoForgeRegistryHelper.register(bus);
    }

    public static void registerBindings(final RegisterKeyMappingsEvent event) {
        event.register(Satchels.KEYMAPPING_TOGGLE_SATCHEL.get());
    }

    public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);

        registrar.playToServer(
                ToggleSatchelPacket.TYPE,
                ToggleSatchelPacket.STREAM_CODEC,
                (p, cx) -> ToggleSatchelPacket.handle((ServerPlayer) cx.player())
        );
    }
}
