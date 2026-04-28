package net.vercte.satchels.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.content.satchel.SatchelData;
import org.jetbrains.annotations.NotNull;

public record SatchelOffsetUpdatePacketC2S(int offset) implements CustomPacketPayload {
    public static final ResourceLocation ID = Satchels.at("satchel_offset_update");
    public static final CustomPacketPayload.Type<SatchelOffsetUpdatePacketC2S> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<ByteBuf, SatchelOffsetUpdatePacketC2S> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, SatchelOffsetUpdatePacketC2S::offset, SatchelOffsetUpdatePacketC2S::new);

    public static void handle(SatchelOffsetUpdatePacketC2S packet, ServerPlayer player) {
        SatchelData satchelData = SatchelData.get(player);
        satchelData.setHotbarOffset(packet.offset());
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
