package net.vercte.satchels.network;

import com.mojang.logging.LogUtils;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.platform.Services;
import org.jetbrains.annotations.NotNull;

public enum ToggleSatchelPacket implements CustomPacketPayload {
    INSTANCE;

    public static final ResourceLocation ID = Satchels.at("toggle_satchel");
    public static final CustomPacketPayload.Type<ToggleSatchelPacket> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<Object, ToggleSatchelPacket> STREAM_CODEC = StreamCodec.unit(ToggleSatchelPacket.INSTANCE);

    public void send() {
        Services.NETWORK.sendPacketC2S(INSTANCE);
    }

    public static void handle(ServerPlayer player) {
        LogUtils.getLogger().info("You're toggling! {}", player);
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
