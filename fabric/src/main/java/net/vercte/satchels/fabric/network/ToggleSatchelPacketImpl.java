package net.vercte.satchels.fabric.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.vercte.satchels.network.ToggleSatchelPacket;

@SuppressWarnings("unused")
public class ToggleSatchelPacketImpl {
    public void send() {
        ClientPlayNetworking.send(ToggleSatchelPacket.INSTANCE);
    }
}
