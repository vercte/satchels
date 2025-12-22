package net.vercte.satchels.neoforge.client;

import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.SatchelsClient;
import net.vercte.satchels.client.SatchelHotbarOverlay;
import net.vercte.satchels.client.SatchelLayer;
import net.vercte.satchels.config.ClientConfig;
import net.vercte.satchels.network.ClientConfigUpdatePacketC2S;
import net.vercte.satchels.satchel.SatchelData;

@Mod(value = Satchels.ID, dist = Dist.CLIENT)
public class SatchelsNeoForgeClient {
    public SatchelsNeoForgeClient(IEventBus modEventBus) {
        SatchelsClient.init();

        NeoForge.EVENT_BUS.addListener(SatchelsNeoForgeClient::endClientTick);
        NeoForge.EVENT_BUS.addListener(SatchelsNeoForgeClient::onLocalPlayerJoin);
        NeoForge.EVENT_BUS.addListener(SatchelsNeoForgeClient::onPlayerRespawn);

        modEventBus.addListener(SatchelsNeoForgeClient::registerOverlays);
        modEventBus.addListener(SatchelsNeoForgeClient::addEntityRenderLayers);
        modEventBus.addListener(NeoForgeDetachedModelPopulator::onRegisterAdditional);
        modEventBus.addListener(NeoForgeDetachedModelPopulator::onBakingCompleted);
    }

    public static void endClientTick(final ClientTickEvent.Post event) {
        SatchelsClient.endClientTick();
    }

    public static void registerOverlays(final RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, Satchels.at(SatchelHotbarOverlay.ID), SatchelHotbarOverlay.INSTANCE::render);
    }

    public static void addEntityRenderLayers(final EntityRenderersEvent.AddLayers event) {
        EntityRenderDispatcher erDispatcher = event.getContext().getEntityRenderDispatcher();
        BlockRenderDispatcher brDispatcher = event.getContext().getBlockRenderDispatcher();

        for(EntityRenderer<? extends Player> renderer : erDispatcher.getSkinMap().values()) {
            if(renderer instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new SatchelLayer<>(playerRenderer, brDispatcher));
            }
        }
    }

    public static void onLocalPlayerJoin(final ClientPlayerNetworkEvent.LoggingIn event) {
        int satchelOffset = ClientConfig.getSatchelOffset();
        SatchelData.get(event.getPlayer()).setSatchelOffset(satchelOffset);
        ClientConfigUpdatePacketC2S.send(satchelOffset);
    }

    public static void onPlayerRespawn(final ClientPlayerNetworkEvent.Clone event) {
        Player player = event.getPlayer();
        if(!player.isLocalPlayer()) return;

        int satchelOffset = ClientConfig.getSatchelOffset();
        SatchelData.get(event.getPlayer()).setSatchelOffset(satchelOffset);
    }
}
