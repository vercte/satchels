package net.vercte.satchels.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.SatchelsClient;
import net.vercte.satchels.satchel.SatchelHotbarOverlay;

@EventBusSubscriber
@Mod(value = Satchels.ID, dist = Dist.CLIENT)
public class SatchelsNeoForgeClient {
    @SubscribeEvent
    public static void endClientTick(final ClientTickEvent.Post event) {
        SatchelsClient.endClientTick();
    }

    @SubscribeEvent
    public static void registerOverlays(final RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, Satchels.at("satchel_hotbar"), SatchelHotbarOverlay::render);
    }
}
