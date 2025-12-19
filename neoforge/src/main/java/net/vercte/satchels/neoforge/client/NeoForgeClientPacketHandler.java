package net.vercte.satchels.neoforge.client;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vercte.satchels.network.SatchelStatusPacketS2C;

public class NeoForgeClientPacketHandler {
    public static void handleSatchelStatusPacketS2C(SatchelStatusPacketS2C packet, IPayloadContext cx) {
        SatchelStatusPacketS2C.handle(packet, cx.player());
    }
}
