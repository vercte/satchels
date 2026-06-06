package net.vercte.satchels.mixin.menu;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.ModTags;
import net.vercte.satchels.content.satchel.SatchelData;
import net.vercte.satchels.content.satchel.SatchelEquipmentSlot;
import net.vercte.satchels.content.satchel.SatchelInventorySlot;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Debug(export = true)
@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @Shadow
    @Final
    public NonNullList<Slot> slots;

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

    @Definition(id = "p_150432_", local = @Local(type = int.class, ordinal = 1, argsOnly = true))
    @Expression("p_150432_ < 9")
    @ModifyExpressionValue(method = "doClick", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean satchels$allowSwappingFromSatchelHotbar(boolean original, int to, int from, ClickType p_150433_, Player p_150434_) {
        return original || this.slots.get(from) instanceof SatchelInventorySlot;
    }

    @Definition(id = "SWAP", field = "Lnet/minecraft/world/inventory/ClickType;SWAP:Lnet/minecraft/world/inventory/ClickType;")
    @Expression("? == SWAP")
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getItem(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 0), slice = @Slice(from = @At("MIXINEXTRAS:EXPRESSION")))
    private ItemStack satchels$getFromSatchelHotbar(Inventory instance, int slotIndex, Operation<ItemStack> original, int p_150431_, int p_150432_, ClickType p_150433_, Player player) {
        SatchelData satchelData = SatchelData.get(player);
        Slot slot = this.slots.get(slotIndex);
        return slot instanceof SatchelInventorySlot ?
                satchelData.getSatchelInventory().getItem(slot.getContainerSlot()) :
                original.call(instance, slotIndex);
    }

    @Definition(id = "SWAP", field = "Lnet/minecraft/world/inventory/ClickType;SWAP:Lnet/minecraft/world/inventory/ClickType;")
    @Expression("? == SWAP")
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setItem(ILnet/minecraft/world/item/ItemStack;)V"), slice = @Slice(from = @At("MIXINEXTRAS:EXPRESSION")))
    private void satchels$setToSatchelHotbar(Inventory instance, int slotIndex, ItemStack stack, Operation<Void> original, int p_150431_, int p_150432_, ClickType p_150433_, Player player) {
        SatchelData satchelData = SatchelData.get(player);
        Slot slot = this.slots.get(slotIndex);
        if(slot instanceof SatchelInventorySlot) satchelData.getSatchelInventory().setItem(slot.getContainerSlot(), stack);
        else original.call(instance, slotIndex, stack);
    }
}
