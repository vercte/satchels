package net.vercte.satchels.mixin.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.vercte.satchels.satchel.MenuWithSatchel;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconMenu.class)
public abstract class BeaconMenuMixin extends AbstractContainerMenu {
    public BeaconMenuMixin(MenuType<?> menuType, int i) { super(menuType, i); }

    @SuppressWarnings("Convert2MethodRef")
    @Inject(method = "<init>(ILnet/minecraft/world/Container;Lnet/minecraft/world/inventory/ContainerData;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    public void addMoreSlots(int i, Container container, ContainerData containerData, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
        if(container instanceof Inventory inventory) {
            SatchelData satchelData = SatchelData.get(inventory.player);

            MenuWithSatchel.addInventorySlots(satchelData, s -> this.addSlot(s), 36, 223, 18);
        }
    }
}
