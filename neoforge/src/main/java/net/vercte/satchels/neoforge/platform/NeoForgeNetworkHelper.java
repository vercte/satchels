package net.vercte.satchels.neoforge.platform;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.vercte.satchels.platform.services.INetworkHelper;

public class NeoForgeNetworkHelper implements INetworkHelper {
    @Override
    public void sendPacketC2S(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }
}
