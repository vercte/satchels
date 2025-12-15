package net.vercte.satchels.platform.services;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface INetworkHelper {
    void sendPacketC2S(CustomPacketPayload payload);
}
