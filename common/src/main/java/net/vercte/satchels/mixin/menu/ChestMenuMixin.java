package net.vercte.satchels.mixin.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.vercte.satchels.satchel.MenuWithSatchel;
import net.vercte.satchels.satchel.SatchelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestMenu.class)
public abstract class ChestMenuMixin extends AbstractContainerMenu {
    @Shadow
    @Final
    private int containerRows;

    protected ChestMenuMixin(@Nullable MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @SuppressWarnings("Convert2MethodRef")
    @Inject(method = "<init>(Lnet/minecraft/world/inventory/MenuType;ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;I)V", at = @At("TAIL"))
    public void addMoreSlots(MenuType<?> menuType, int i, Inventory inventory, Container container, int j, CallbackInfo ci) {
        SatchelData satchelData = SatchelData.get(inventory.player);

        MenuWithSatchel.addInventorySlots(satchelData, s -> this.addSlot(s), 8, 189 + (this.containerRows - 4) * 18, 18);
    }
}
