package net.vercte.satchels.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.content.satchel.SatchelData;
import org.jetbrains.annotations.NotNull;

public record SatchelStatusPacketS2C(boolean enabled) implements CustomPacketPayload {
    public static final ResourceLocation ID = Satchels.at("satchel_status");
    public static final CustomPacketPayload.Type<SatchelStatusPacketS2C> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<ByteBuf, SatchelStatusPacketS2C> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, SatchelStatusPacketS2C::enabled, SatchelStatusPacketS2C::new);

    public static void handle(SatchelStatusPacketS2C packet, Player player) {
        SatchelData satchelData = SatchelData.get(player);
        satchelData.setActive(packet.enabled, false);
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
