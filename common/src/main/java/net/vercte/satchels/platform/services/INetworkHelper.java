package net.vercte.satchels.platform.services;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface INetworkHelper {
    void sendPacketC2S(CustomPacketPayload payload);
    void sendPacketS2C(ServerPlayer player, CustomPacketPayload payload);
    void sendPacketToTrackers(Entity entity, CustomPacketPayload payload);
}
