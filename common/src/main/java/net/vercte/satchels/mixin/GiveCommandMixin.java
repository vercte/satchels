package net.vercte.satchels.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GiveCommand.class)
public class GiveCommandMixin {
    @WrapOperation(method = "giveItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"))
    private static boolean prioritizeSatchel(Inventory inventory, ItemStack stack, Operation<Boolean> original, @Local ServerPlayer player) {
        SatchelData satchelData = SatchelData.get(player);
        if(satchelData.isSatchelEnabled()) {
            return satchelData.getSatchelInventory().add(stack) || original.call(inventory, stack);
        }

        return original.call(inventory, stack) || (satchelData.canAccessSatchelInventory() && satchelData.getSatchelInventory().add(stack));
    }
}
