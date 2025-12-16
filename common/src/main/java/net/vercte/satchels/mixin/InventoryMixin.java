package net.vercte.satchels.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Shadow
    @Final
    public Player player;

    @Shadow
    public static boolean isHotbarSlot(int i) { return false; }

    @Shadow
    public int selected;

    @ModifyReturnValue(method = "getSelected", at = @At("RETURN"))
    public ItemStack getSelected(ItemStack original) {
        SatchelData satchelData = SatchelData.get(player);
        if(satchelData.isSlotInSatchel(this.selected) && satchelData.isSatchelEnabled()) {
            return satchelData.getSatchelInventory().getItem(this.selected);
        }
        return original;
    }

    @ModifyReturnValue(method = "getDestroySpeed", at = @At("RETURN"))
    public float getDestroySpeed(float original, BlockState state) {
        SatchelData satchelData = SatchelData.get(player);
        if(satchelData.isSlotInSatchel(this.selected) && satchelData.isSatchelEnabled()) {
            return satchelData.getSatchelInventory().getItem(this.selected).getDestroySpeed(state);
        }
        return original;
    }

    @Inject(method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At("HEAD"), cancellable = true)
    public void placeItemBackInInventory(ItemStack stack, boolean update, CallbackInfo ci) {
        SatchelData satchelData = SatchelData.get(player);
        if(satchelData.isSatchelEnabled()) {
            boolean added = satchelData.getSatchelInventory().placeItemBackInInventory(stack, update);
            if(added) ci.cancel();
        }
    }
}
