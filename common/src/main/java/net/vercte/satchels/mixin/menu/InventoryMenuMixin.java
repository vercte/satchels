package net.vercte.satchels.mixin.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.vercte.satchels.satchel.MenuWithSatchel;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends RecipeBookMenu<CraftingInput, CraftingRecipe> {
    public InventoryMenuMixin(MenuType<?> menuType, int i) { super(menuType, i); }

    @SuppressWarnings("Convert2MethodRef")
    @Inject(method = "<init>", at = @At("TAIL"))
    public void addMoreSlots(Inventory inventory, boolean bl, Player player, CallbackInfo ci) {
        SatchelData satchelData = SatchelData.get(player);

//        this.addSlot(new SatchelEquipmentSlot(satchelData.getSatchelSlotInventory(), 152, 62));
        MenuWithSatchel.addInventorySlots(satchelData, s -> this.addSlot(s), 8, 170, 18);
    }

//    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
//    public void quickMoveStack(Player player, int i, CallbackInfoReturnable<ItemStack> cir) {
//        Slot slot = this.slots.get(i);
//        ItemStack inSlot = slot.getItem();
//
//        if(inSlot.is(ModItems.SATCHEL.get())) {
//            Optional<Slot> possibleSatchelSlot = this.slots.stream().filter(t -> t instanceof SatchelEquipmentSlot).findFirst();
//            if(possibleSatchelSlot.isPresent()) {
//                Slot satchelSlot = possibleSatchelSlot.get();
//                if(satchelSlot.hasItem()) return;
//
//                int satchelSlotIndex = this.slots.indexOf(possibleSatchelSlot.get());
//
//                if (this.moveItemStackTo(inSlot, satchelSlotIndex, satchelSlotIndex+1, false)) {
//                    cir.setReturnValue(inSlot.copy());
//                }
//            }
//        }
//    }
}
