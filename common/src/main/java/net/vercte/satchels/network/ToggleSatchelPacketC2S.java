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

public record ToggleSatchelPacketC2S(boolean enabled) implements CustomPacketPayload {
    public static final ResourceLocation ID = Satchels.at("toggle_satchel");
    public static final CustomPacketPayload.Type<ToggleSatchelPacketC2S> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<ByteBuf, ToggleSatchelPacketC2S> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, ToggleSatchelPacketC2S::enabled, ToggleSatchelPacketC2S::new);

    public static void send(boolean enabled) {
        Services.NETWORK.sendPacketC2S(new ToggleSatchelPacketC2S(enabled));
    }

    public static void handle(ToggleSatchelPacketC2S packet, ServerPlayer player) {
        SatchelData satchelData = SatchelData.get(player);
        if(!satchelData.canAccessSatchelInventory()) satchelData.setSatchelEnabled(false);
        else {
            satchelData.setSatchelEnabled(packet.enabled);
        }
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
