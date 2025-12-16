package net.vercte.satchels.platform.services;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface INetworkHelper {
    void sendPacketC2S(CustomPacketPayload payload);
    void sendPacketS2C(ServerPlayer player, CustomPacketPayload payload);
}
