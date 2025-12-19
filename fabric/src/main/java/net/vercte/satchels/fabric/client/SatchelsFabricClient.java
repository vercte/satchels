package net.vercte.satchels.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.vercte.satchels.SatchelsClient;
import net.vercte.satchels.config.ClientConfig;
import net.vercte.satchels.network.ClientConfigUpdatePacketC2S;
import net.vercte.satchels.network.SatchelStatusPacketS2C;
import net.vercte.satchels.satchel.SatchelData;
import net.vercte.satchels.satchel.SatchelHotbarOverlay;

public final class SatchelsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SatchelsClient.init();

        KeyBindingHelper.registerKeyBinding(SatchelsClient.KEYMAPPING_TOGGLE_SATCHEL.get());

        ClientPlayNetworking.registerGlobalReceiver(SatchelStatusPacketS2C.TYPE, (p, cx) -> SatchelStatusPacketS2C.handle(p, cx.player()));

        HudRenderCallback.EVENT.register(SatchelHotbarOverlay::render);
        ClientTickEvents.END_CLIENT_TICK.register(t -> SatchelsClient.endClientTick());

        ClientPlayConnectionEvents.JOIN.register((c, p, mc) -> {
            int satchelOffset = ClientConfig.getSatchelOffset();
            SatchelData.get(mc.player).setSatchelOffset(satchelOffset);
            ClientConfigUpdatePacketC2S.send(satchelOffset);
        });
    }
}
