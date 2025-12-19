package net.vercte.satchels.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.vercte.satchels.SatchelsClient;
import net.vercte.satchels.client.SatchelHotbarOverlay;
import net.vercte.satchels.client.SatchelLayer;
import net.vercte.satchels.client.model.DetachedModel;
import net.vercte.satchels.config.ClientConfig;
import net.vercte.satchels.network.ClientConfigUpdatePacketC2S;
import net.vercte.satchels.network.SatchelStatusPacketS2C;
import net.vercte.satchels.satchel.SatchelData;

public final class SatchelsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SatchelsClient.init();

        KeyBindingHelper.registerKeyBinding(SatchelsClient.KEYMAPPING_TOGGLE_SATCHEL.get());
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(SatchelsFabricClient::addEntityRendererLayers);

        ClientPlayNetworking.registerGlobalReceiver(SatchelStatusPacketS2C.TYPE, (p, cx) -> SatchelStatusPacketS2C.handle(p, cx.player()));

        HudRenderCallback.EVENT.register(SatchelHotbarOverlay::render);
        ClientPlayConnectionEvents.JOIN.register(SatchelsFabricClient::onJoinServer);
        ClientTickEvents.END_CLIENT_TICK.register(t -> SatchelsClient.endClientTick());

        ModelLoadingPlugin.register(c -> c.addModels(DetachedModel.ALL.keySet()));
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new DetachedModelReloadListener());
    }

    public static void addEntityRendererLayers(EntityType<? extends LivingEntity> entityType, LivingEntityRenderer<?, ?> entityRenderer,
                                               RegistrationHelper registrationHelper, EntityRendererProvider.Context context) {
        BlockRenderDispatcher brDispatcher = context.getBlockRenderDispatcher();

        if(entityRenderer instanceof PlayerRenderer playerRenderer) {
            registrationHelper.register(new SatchelLayer<>(playerRenderer, brDispatcher));
        }
    }

    public static void onJoinServer(ClientPacketListener clientPacketListener, PacketSender packetSender, Minecraft mc) {
        int satchelOffset = ClientConfig.getSatchelOffset();
        SatchelData.get(mc.player).setSatchelOffset(satchelOffset);
        ClientConfigUpdatePacketC2S.send(satchelOffset);
    }
}
