package net.vercte.satchels.mixin.compat.quark;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.vercte.satchels.content.satchel.SatchelEquipmentSlot;
import net.vercte.satchels.content.satchel.SatchelInventorySlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.quark.addons.oddities.inventory.BackpackMenu;

@Mixin(BackpackMenu.class)
public class BackpackMenuMixin {
    @Definition(id = "container", field = "Lnet/minecraft/world/inventory/Slot;container:Lnet/minecraft/world/Container;")
    @Definition(id = "inventory", local = @Local(type = Inventory.class, name = "inventory"))
    @Expression("?.container == inventory")
    @ModifyExpressionValue(method = "<init>", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean alsoAllowSatchelSlots(boolean original, int windowId, Player player, @Local(name = "slot") Slot slot) {
        return original || slot instanceof SatchelEquipmentSlot || slot instanceof SatchelInventorySlot;
    }
}
