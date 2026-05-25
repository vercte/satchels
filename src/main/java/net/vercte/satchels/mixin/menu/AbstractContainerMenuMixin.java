package net.vercte.satchels.mixin.menu;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.ModTags;
import net.vercte.satchels.content.satchel.SatchelEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @WrapOperation(
            method = "doClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/Slot;mayPickup(Lnet/minecraft/world/entity/player/Player;)Z",
                    ordinal = 1
            )
    )
    private boolean satchels$allowSwappingSatchelEquipment(Slot slot, Player player, Operation<Boolean> original, @Local(ordinal = 0) ItemStack held, @Local(ordinal = 1) ItemStack contained) {
        if(original.call(slot, player)) return true;

        if(slot instanceof SatchelEquipmentSlot) {
            return held.is(ModTags.SATCHEL) && contained.is(ModTags.SATCHEL);
        }

        return false;
    }
}
