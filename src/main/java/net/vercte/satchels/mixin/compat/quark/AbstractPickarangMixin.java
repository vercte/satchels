package net.vercte.satchels.mixin.compat.quark;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.compat.quark.SatchelsPickarang;
import net.vercte.satchels.content.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.quark.content.tools.entity.rang.AbstractPickarang;

@Mixin(AbstractPickarang.class)
public class AbstractPickarangMixin implements SatchelsPickarang {
    @Unique
    private boolean satchels$slotIsInSatchel = false;

    @WrapOperation(method = "onHitBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private void satchels$setInSatchelIfActive(Inventory instance, int slot, ItemStack stack, Operation<Void> original) {
        SatchelData satchelData = SatchelData.get(instance.player);
        if(!satchelData.isSlotInSatchel(slot) || !satchelData.isActive() || !satchelData.canAccess()) {
            original.call(instance, slot, stack);
            return;
        }

        int sSlot = satchelData.convertToSatchelIndex(slot);
        satchelData.getSatchelInventory().setItem(sSlot, stack);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getItem(I)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack satchels$getFromSatchelIfReturning(Inventory instance, int slot, Operation<ItemStack> original) {
        SatchelData satchelData = SatchelData.get(instance.player);
        if(!satchels$slotIsInSatchel || !satchelData.isSlotInSatchel(slot) || !satchelData.canAccess()) return original.call(instance, slot);;

        int sSlot = satchelData.convertToSatchelIndex(slot);
        return satchelData.getSatchelInventory().getItem(sSlot);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private void satchels$setInSatchelIfReturning(Inventory instance, int slot, ItemStack stack, Operation<Void> original) {
        SatchelData satchelData = SatchelData.get(instance.player);
        if(!satchels$slotIsInSatchel || !satchelData.isSlotInSatchel(slot) || !satchelData.canAccess()) {
            original.call(instance, slot, stack);
            return;
        }

        int sSlot = satchelData.convertToSatchelIndex(slot);
        satchelData.getSatchelInventory().setItem(sSlot, stack);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void satchels$addSlotStatus(CompoundTag compound, CallbackInfo ci) {
        if(satchels$slotIsInSatchel) compound.putBoolean(TAG_SLOT_IS_IN_SATCHEL, true);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void satchels$readSlotStatus(CompoundTag compound, CallbackInfo ci) {
        if(compound.getBoolean(TAG_SLOT_IS_IN_SATCHEL)) satchels$slotIsInSatchel = true;
    }

    @Override
    public void satchels$slotIsInSatchel() {
        satchels$slotIsInSatchel = true;
    }
}
