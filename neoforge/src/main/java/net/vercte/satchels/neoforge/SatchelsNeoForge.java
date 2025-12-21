package net.vercte.satchels.neoforge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.vercte.satchels.Satchels;
import net.neoforged.fml.common.Mod;
import net.vercte.satchels.SatchelsClient;
import net.vercte.satchels.neoforge.client.NeoForgeClientPacketHandler;
import net.vercte.satchels.neoforge.datagen.SatchelsDatagen;
import net.vercte.satchels.neoforge.platform.NeoForgeRegistryHelper;
import net.vercte.satchels.network.ClientConfigUpdatePacketC2S;
import net.vercte.satchels.network.SatchelStatusPacketS2C;
import net.vercte.satchels.network.ToggleSatchelPacketC2S;
import net.vercte.satchels.satchel.SatchelData;

@Mod(Satchels.ID)
public final class SatchelsNeoForge {
    public SatchelsNeoForge(IEventBus bus) {
        Satchels.init();

        NeoForgeRegistryHelper.register(bus);

        bus.addListener(SatchelsNeoForge::registerBindings);
        bus.addListener(SatchelsNeoForge::registerPayloadHandlers);
        bus.addListener(SatchelsDatagen::gatherData);
//        NeoForge.EVENT_BUS.addListener(SatchelsNeoForge::sendSatchelData);
        NeoForge.EVENT_BUS.addListener(SatchelsNeoForge::playerJoin);
    }

    public static void registerBindings(final RegisterKeyMappingsEvent event) {
        event.register(SatchelsClient.KEYMAPPING_TOGGLE_SATCHEL.get());
    }

    public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);

        registrar.playToServer(
                ToggleSatchelPacketC2S.TYPE,
                ToggleSatchelPacketC2S.STREAM_CODEC,
                (p, cx) -> ToggleSatchelPacketC2S.handle(p, (ServerPlayer) cx.player())
        );

        registrar.playToServer(
                ClientConfigUpdatePacketC2S.TYPE,
                ClientConfigUpdatePacketC2S.STREAM_CODEC,
                (p, cx) -> ClientConfigUpdatePacketC2S.handle(p, (ServerPlayer) cx.player())
        );

        registrar.playToClient(
                SatchelStatusPacketS2C.TYPE,
                SatchelStatusPacketS2C.STREAM_CODEC,
                NeoForgeClientPacketHandler::handleSatchelStatusPacket
        );

//        registrar.playToClient(
//                SatchelSlotUpdatePacketS2C.TYPE,
//                SatchelSlotUpdatePacketS2C.STREAM_CODEC,
//                NeoForgeClientPacketHandler::handleSatchelSlotUpdatePacket
//        );
    }

//    public static void sendSatchelData(PlayerEvent.StartTracking event) {
//        if(!(event.getTarget() instanceof Player targetPlayer)) return;
//        SatchelData.updateTrackedSatchelsForPlayer(event.getEntity(), targetPlayer);
//    }

    public static void playerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if(!(player instanceof ServerPlayer sp)) return;

        SatchelData.get(sp).updateClient();
    }
}
