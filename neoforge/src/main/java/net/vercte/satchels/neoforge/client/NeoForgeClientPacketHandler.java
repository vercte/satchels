package net.vercte.satchels.neoforge.client;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vercte.satchels.network.SatchelStatusPacketS2C;

public class NeoForgeClientPacketHandler {
    public static void handleSatchelStatusPacket(SatchelStatusPacketS2C packet, IPayloadContext cx) {
        SatchelStatusPacketS2C.handle(packet, cx.player());
    }

//    public static void handleSatchelSlotUpdatePacket(SatchelSlotUpdatePacketS2C packet, IPayloadContext cx) {
//        SatchelSlotUpdatePacketS2C.handle(packet, cx.player().level());
//    }
}
