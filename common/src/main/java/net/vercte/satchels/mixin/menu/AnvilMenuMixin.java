package net.vercte.satchels.mixin.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.vercte.satchels.satchel.MenuWithSatchel;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    public AnvilMenuMixin(MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) { super(menuType, i, inventory, containerLevelAccess); }

    @SuppressWarnings("Convert2MethodRef")
    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    public void addMoreSlots(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
        SatchelData satchelData = SatchelData.get(this.player);

        MenuWithSatchel.addInventorySlots(satchelData, s -> this.addSlot(s), 8, 170, 18);
    }
}
