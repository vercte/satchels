package net.vercte.satchels.fabric.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.vercte.satchels.platform.services.INetworkHelper;

public class FabricNetworkHelper implements INetworkHelper {
    @Override
    public void sendPacketC2S(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }
}
