package net.vercte.satchels.neoforge.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.SatchelsClient;
import net.vercte.satchels.config.ClientConfig;
import net.vercte.satchels.network.ClientConfigUpdatePacketC2S;
import net.vercte.satchels.satchel.SatchelData;
import net.vercte.satchels.satchel.SatchelHotbarOverlay;

@EventBusSubscriber
@Mod(value = Satchels.ID, dist = Dist.CLIENT)
public class SatchelsNeoForgeClient {
    public SatchelsNeoForgeClient() {
        SatchelsClient.init();
    }

    @SubscribeEvent
    public static void endClientTick(final ClientTickEvent.Post event) {
        SatchelsClient.endClientTick();
    }

    @SubscribeEvent
    public static void registerOverlays(final RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, Satchels.at("satchel_hotbar"), SatchelHotbarOverlay::render);
    }

    @SubscribeEvent
    public static void onLocalPlayerJoin(final ClientPlayerNetworkEvent.LoggingIn event) {
        int satchelOffset = ClientConfig.getSatchelOffset();
        SatchelData.get(Minecraft.getInstance().player).setSatchelOffset(satchelOffset);
        ClientConfigUpdatePacketC2S.send(satchelOffset);
    }
}
