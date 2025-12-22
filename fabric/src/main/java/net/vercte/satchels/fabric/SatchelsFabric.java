package net.vercte.satchels.fabric;

import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.vercte.satchels.ModItems;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.fabric.compat.trinkets.SatchelTrinket;
import net.vercte.satchels.network.ClientConfigUpdatePacketC2S;
import net.vercte.satchels.network.SatchelStatusPacketS2C;
import net.vercte.satchels.network.ToggleSatchelPacketC2S;
import net.vercte.satchels.satchel.SatchelData;

public final class SatchelsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Satchels.init();

        PayloadTypeRegistry.playS2C().register(SatchelStatusPacketS2C.TYPE, SatchelStatusPacketS2C.STREAM_CODEC);
//        PayloadTypeRegistry.playS2C().register(SatchelSlotUpdatePacketS2C.TYPE, SatchelSlotUpdatePacketS2C.STREAM_CODEC);

        PayloadTypeRegistry.playC2S().register(ToggleSatchelPacketC2S.TYPE, ToggleSatchelPacketC2S.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(ClientConfigUpdatePacketC2S.TYPE, ClientConfigUpdatePacketC2S.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ToggleSatchelPacketC2S.TYPE, (p, cx) -> ToggleSatchelPacketC2S.handle(p, cx.player()));
        ServerPlayNetworking.registerGlobalReceiver(ClientConfigUpdatePacketC2S.TYPE, (p, cx) -> ClientConfigUpdatePacketC2S.handle(p, cx.player()));

        ServerPlayerEvents.JOIN.register(t -> SatchelData.get(t).updateClient());
        ServerPlayerEvents.AFTER_RESPAWN.register(this::onPlayerRespawn);

        TrinketsApi.registerTrinket(ModItems.SATCHEL.get(), new SatchelTrinket());
    }

    public void onPlayerRespawn(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
        SatchelData oldSatchelData = SatchelData.get(oldPlayer);
        SatchelData newSatchelData = SatchelData.get(newPlayer);
        newSatchelData.setSatchelOffset(oldSatchelData.getSatchelOffset());

        GameRules gameRules = newPlayer.level().getGameRules();
        if(gameRules.getBoolean(GameRules.RULE_KEEPINVENTORY))
            oldSatchelData.copyItemsTo(newSatchelData);
    }
}
