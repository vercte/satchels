package net.vercte.satchels.neoforge.mixin.compat.curios;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.vercte.satchels.satchel.MenuWithSatchel;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;

@Mixin(CuriosContainer.class)
public abstract class CuriosMenuMixin extends RecipeBookMenu<RecipeInput, Recipe<RecipeInput>> {
    @Shadow
    @Final
    public Player player;

    public CuriosMenuMixin(MenuType<?> arg, int i) {
        super(arg, i);
    }

    @SuppressWarnings("Convert2MethodRef")
    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;)V", at = @At("TAIL"))
    public void addMoreSlots(int windowId, Inventory playerInventory, CallbackInfo ci) {
        SatchelData satchelData = SatchelData.get(playerInventory.player);

//        this.addSlot(new SatchelEquipmentSlot(satchelData.getSatchelSlotInventory(), 152, 62));
        MenuWithSatchel.addInventorySlots(satchelData, s -> this.addSlot(s), 8, 170, 18);
    }
}
