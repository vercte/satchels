package net.vercte.satchels.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.platform.Services;
import net.vercte.satchels.satchel.SatchelData;

public record SatchelStatusPacketS2C(boolean enabled) implements CustomPacketPayload {
    public static final ResourceLocation ID = Satchels.at("satchel_status");
    public static final CustomPacketPayload.Type<SatchelStatusPacketS2C> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<ByteBuf, SatchelStatusPacketS2C> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, SatchelStatusPacketS2C::enabled, SatchelStatusPacketS2C::new);

    public static void send(ServerPlayer player, boolean enabled) {
        Services.NETWORK.sendPacketS2C(player, new SatchelStatusPacketS2C(enabled));
    }

    public static void handle(SatchelStatusPacketS2C packet) {
        Player player = Minecraft.getInstance().player;

        SatchelData satchelData = SatchelData.get(player);
        satchelData.setSatchelEnabled(packet.enabled);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
