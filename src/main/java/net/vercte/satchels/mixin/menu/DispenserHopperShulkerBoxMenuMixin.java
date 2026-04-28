package net.vercte.satchels.mixin.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.vercte.satchels.api.MenuWithSatchel;
import net.vercte.satchels.content.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({DispenserMenu.class, HopperMenu.class, ShulkerBoxMenu.class})
public abstract class DispenserHopperShulkerBoxMenuMixin extends AbstractContainerMenu {
    public DispenserHopperShulkerBoxMenuMixin(MenuType<?> menuType, int i) { super(menuType, i); }

    @SuppressWarnings("Convert2MethodRef")
    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;)V", at = @At("TAIL"))
    public void addMoreSlots(int i, Inventory inventory, Container container, CallbackInfo ci) {
        SatchelData satchelData = SatchelData.get(inventory.player);

        int yOffset = 0;
        if((Object)this instanceof HopperMenu) yOffset = -33;
        if((Object)this instanceof ShulkerBoxMenu) yOffset = 1;

        MenuWithSatchel.addInventorySlots(satchelData, s -> this.addSlot(s), 8, 170 + yOffset, 18);
    }
}
