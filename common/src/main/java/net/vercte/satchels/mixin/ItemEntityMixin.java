package net.vercte.satchels.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @WrapOperation(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"))
    public boolean playerTouch(Inventory inventory, ItemStack stack, Operation<Boolean> original) {
        SatchelData satchelData = SatchelData.get(inventory.player);
        boolean addToSatchel = satchelData.isSatchelEnabled() && satchelData.getSatchelInventory().add(stack);
        return addToSatchel || original.call(inventory, stack);
    }
}
