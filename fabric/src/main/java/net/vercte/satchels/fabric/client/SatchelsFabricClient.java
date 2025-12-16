package net.vercte.satchels.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.SatchelsClient;
import net.vercte.satchels.network.SatchelStatusPacketS2C;
import net.vercte.satchels.satchel.SatchelHotbarOverlay;

public final class SatchelsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(Satchels.KEYMAPPING_TOGGLE_SATCHEL.get());
        ClientTickEvents.END_CLIENT_TICK.register(t -> SatchelsClient.endClientTick());
        HudRenderCallback.EVENT.register(SatchelHotbarOverlay::render);

        ClientPlayNetworking.registerGlobalReceiver(SatchelStatusPacketS2C.TYPE, (p, cx) -> SatchelStatusPacketS2C.handle(p));
    }
}
