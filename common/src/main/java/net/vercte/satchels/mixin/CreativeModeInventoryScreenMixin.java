package net.vercte.satchels.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.vercte.satchels.satchel.SatchelEquipmentSlot;
import net.vercte.satchels.satchel.SatchelInventorySlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {
    @WrapOperation(method = "selectTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;add(Ljava/lang/Object;)Z", ordinal = 2))
    public boolean dontAddSatchelsSlots(NonNullList<?> list, Object object, Operation<Boolean> original) {
        if(object instanceof CreativeModeInventoryScreen.SlotWrapper slotWrapper) {
            if(slotWrapper.target instanceof SatchelEquipmentSlot) return false;
            if(slotWrapper.target instanceof SatchelInventorySlot) return false;
        }
        return original.call(list, object);
    }
}
