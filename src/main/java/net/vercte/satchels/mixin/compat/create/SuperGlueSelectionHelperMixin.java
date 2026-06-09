package net.vercte.satchels.mixin.compat.create;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.glue.SuperGlueItem;
import com.simibubi.create.content.contraptions.glue.SuperGlueSelectionHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.content.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SuperGlueSelectionHelper.class)
public class SuperGlueSelectionHelperMixin {
    @Inject(method = "collectGlueFromInventory", at = @At("RETURN"), cancellable = true)
    private static void satchels$scanSatchelToo(Player player, int requiredAmount, boolean simulate, CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) int requiredAmountCurrent) {
        SatchelData satchelData = SatchelData.get(player);

        for(int i = 0; i < satchelData.getSatchelInventory().getItems().size(); i++) {
            ItemStack stack = satchelData.getSatchelInventory().getItem(i);
            if (stack.isEmpty())
                continue;
            if (!(stack.getItem() instanceof SuperGlueItem))
                continue;

            int charges = Math.min(requiredAmount, stack.getMaxDamage() - stack.getDamageValue());

            stack.hurtAndBreak(charges, player, EquipmentSlot.MAINHAND);

            requiredAmount -= charges;
            if (requiredAmount <= 0) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
