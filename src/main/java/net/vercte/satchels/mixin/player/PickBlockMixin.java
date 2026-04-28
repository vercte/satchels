package net.vercte.satchels.mixin.player;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.vercte.satchels.network.packets.ToggleSatchelPacketC2S;
import net.vercte.satchels.content.satchel.SatchelData;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class PickBlockMixin {
    @Inject(method = "pickBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;findSlotMatchingItem(Lnet/minecraft/world/item/ItemStack;)I"), cancellable = true)
    public void checkSatchelAfter(CallbackInfo ci, @Local ItemStack stack, @Local Inventory inventory, @Local boolean creative) {
        SatchelData data = SatchelData.get(inventory.player);
        int invSlot = inventory.findSlotMatchingItem(stack);
        if(!creative && (data.isActive() || invSlot == -1 || invSlot > 8)) {
            int slot = data.getSatchelInventory().findSlotMatchingItem(stack);

            if(slot == -1) return;
            if(invSlot != -1 && invSlot < 9 && slot + data.getHotbarOffset() > invSlot) return;
            inventory.selected = slot + data.getHotbarOffset();

            ci.cancel();

            if(data.isActive()) return;
            data.setActive(true, true);
            PacketDistributor.sendToServer(new ToggleSatchelPacketC2S(true));
        }
    }

    @Inject(method = "pickBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Inventory;selected:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    public void deselectSatchelIfNeeded(CallbackInfo ci, @Local Inventory inventory) {
        SatchelData data = SatchelData.get(inventory.player);
        if(!data.isSlotInSatchel(inventory.selected)) return;

        if(!data.isActive()) return;
        data.setActive(false, true);
        PacketDistributor.sendToServer(new ToggleSatchelPacketC2S(false));
    }
}
