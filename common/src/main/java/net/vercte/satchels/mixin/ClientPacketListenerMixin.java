package net.vercte.satchels.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// This is not implemented because it works regardless

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleContainerSetSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/Tutorial;onGetItem(Lnet/minecraft/world/item/ItemStack;)V", ordinal = 0), cancellable = true)
    public void handleContainerSetSlot(ClientboundContainerSetSlotPacket packet, CallbackInfo ci) {
        if(packet.getContainerId() == -2187) {
            ItemStack itemStack = packet.getItem();
            Minecraft.getInstance().getTutorial().onGetItem(itemStack);

            Player player = Minecraft.getInstance().player;

            assert player != null;
            SatchelData.get(player).getSatchelInventory().setItem(packet.getSlot(), itemStack);
            ci.cancel();
        }
    }
}
