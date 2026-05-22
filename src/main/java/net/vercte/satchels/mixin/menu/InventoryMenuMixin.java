package net.vercte.satchels.mixin.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.vercte.satchels.ModAttachmentTypes;
import net.vercte.satchels.api.MenuWithSatchel;
import net.vercte.satchels.compat.SatchelsCompat;
import net.vercte.satchels.compat.vanilla.SatchelEquipmentSlot;
import net.vercte.satchels.compat.vanilla.SatchelSlotItemStackHandler;
import net.vercte.satchels.compat.vanilla.VanillaCompat;
import net.vercte.satchels.content.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends RecipeBookMenu<CraftingInput, CraftingRecipe> {
    public InventoryMenuMixin(MenuType<?> p_40115_, int p_40116_) {
        super(p_40115_, p_40116_);
    }

    @SuppressWarnings("Convert2MethodRef")
    @Inject(method = "<init>", at = @At("TAIL"))
    public void addMoreSlots(Inventory inventory, boolean bl, Player player, CallbackInfo ci) {
        SatchelData satchelData = SatchelData.get(player);

        if(SatchelsCompat.VANILLA.isLoaded()) this.addSlot(
                new SatchelEquipmentSlot(player, 170 + 8, 142)
        );
        MenuWithSatchel.addInventorySlots(satchelData, s -> this.addSlot(s), 8, 170, 18);
    }
}
