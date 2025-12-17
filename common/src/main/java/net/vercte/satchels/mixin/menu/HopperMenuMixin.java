package net.vercte.satchels.mixin.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.inventory.MenuType;
import net.vercte.satchels.satchel.MenuWithSatchel;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HopperMenu.class)
public abstract class HopperMenuMixin extends AbstractContainerMenu {
    public HopperMenuMixin(MenuType<?> menuType, int i) { super(menuType, i); }

    @SuppressWarnings("Convert2MethodRef")
    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;)V", at = @At("TAIL"))
    public void addMoreSlots(int i, Inventory inventory, Container container, CallbackInfo ci) {
        SatchelData satchelData = SatchelData.get(inventory.player);

        MenuWithSatchel.addInventorySlots(satchelData, s -> this.addSlot(s), 8, 137, 18);
    }
}
