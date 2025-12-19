package net.vercte.satchels.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.platform.Services;
import net.vercte.satchels.satchel.SatchelData;
import org.jetbrains.annotations.NotNull;

public record ClientConfigUpdatePacketC2S(int satchelOffset) implements CustomPacketPayload {
    public static final ResourceLocation ID = Satchels.at("update_config");
    public static final Type<ClientConfigUpdatePacketC2S> TYPE = new Type<>(ID);
    public static final StreamCodec<ByteBuf, ClientConfigUpdatePacketC2S> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, ClientConfigUpdatePacketC2S::satchelOffset, ClientConfigUpdatePacketC2S::new);

    public static void send(int satchelOffset) {
        Services.NETWORK.sendPacketC2S(new ClientConfigUpdatePacketC2S(satchelOffset));
    }

    public static void handle(ClientConfigUpdatePacketC2S packet, ServerPlayer player) {
        SatchelData satchelData = SatchelData.get(player);
        satchelData.setSatchelOffset(packet.satchelOffset);
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
