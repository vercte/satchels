package net.vercte.satchels.fabric;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.vercte.satchels.Satchels;
import net.fabricmc.api.ModInitializer;
import net.vercte.satchels.network.ToggleSatchelPacket;

public final class SatchelsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Satchels.init();

        PayloadTypeRegistry.playC2S().register(ToggleSatchelPacket.TYPE, ToggleSatchelPacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ToggleSatchelPacket.TYPE, (p, cx) -> ToggleSatchelPacket.handle(cx.player()));
    }
}
