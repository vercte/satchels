package net.vercte.satchels.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.SatchelsClient;

public final class SatchelsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(Satchels.KEYMAPPING_TOGGLE_SATCHEL.get());
        ClientTickEvents.END_CLIENT_TICK.register(t -> SatchelsClient.endClientTick());
    }
}
