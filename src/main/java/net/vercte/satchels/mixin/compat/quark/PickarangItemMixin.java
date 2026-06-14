package net.vercte.satchels.mixin.compat.quark;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vercte.satchels.compat.quark.SatchelsPickarang;
import net.vercte.satchels.content.satchel.SatchelData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.violetmoon.quark.content.tools.entity.rang.AbstractPickarang;
import org.violetmoon.quark.content.tools.item.PickarangItem;

@Mixin(PickarangItem.class)
public class PickarangItemMixin {
    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/violetmoon/quark/content/tools/entity/rang/AbstractPickarang;setThrowData(ILnet/minecraft/world/item/ItemStack;)V"
            )
    )
    private void satchels$setSlotIsInSatchel(Level worldIn, Player playerIn, @NotNull InteractionHand handIn, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, @Local(name = "slot") int slot, @Local(name = "entity") AbstractPickarang<?> pickarang) {
        SatchelData satchelData = SatchelData.get(playerIn);
        if(satchelData.isSlotInSatchel(slot) && satchelData.isActive()) {
            ((SatchelsPickarang)pickarang).satchels$slotIsInSatchel();
        }
    }
}
