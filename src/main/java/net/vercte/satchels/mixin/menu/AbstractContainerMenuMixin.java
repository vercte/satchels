package net.vercte.satchels.mixin.menu;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.ModTags;
import net.vercte.satchels.content.satchel.SatchelData;
import net.vercte.satchels.content.satchel.SatchelEquipmentSlot;
import net.vercte.satchels.content.satchel.SatchelInventory;
import net.vercte.satchels.content.satchel.SatchelInventorySlot;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Debug(export = true)
@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @Shadow
    @Final
    public NonNullList<Slot> slots;

    @WrapOperation(
            method = "doClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/Slot;mayPickup(Lnet/minecraft/world/entity/player/Player;)Z",
                    ordinal = 1
            )
    )
    private boolean satchels$allowSwappingSatchelEquipment(Slot slot, Player player, Operation<Boolean> original, @Local(ordinal = 0) ItemStack held, @Local(ordinal = 1) ItemStack contained) {
        if(original.call(slot, player)) return true;

        if(slot instanceof SatchelEquipmentSlot) {
            return held.is(ModTags.SATCHEL) && contained.is(ModTags.SATCHEL);
        }

        return false;
    }

    @Definition(id = "p_150432_", local = @Local(type = int.class, ordinal = 1, argsOnly = true))
    @Expression("p_150432_ < 9")
    @ModifyExpressionValue(method = "doClick", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean satchels$allowSwappingFromSatchelHotbar(boolean original, int to, int from, ClickType p_150433_, Player p_150434_) {
        return original || this.slots.get(from) instanceof SatchelInventorySlot;
    }

    // Shift-clicking (QUICK_MOVE) out of a satchel slot: the container's own quickMoveStack branches on
    // hardcoded slot-index ranges that don't account for the satchel slots appended past the vanilla layout,
    // so it misclassifies the source. Trick the container into treating the click as if it came from a real
    // player-inventory slot: transiently present the satchel item in that slot, call the original with the
    // borrowed menu-slot index, then copy the remainder back and restore the borrowed slot's item.
    //
    // We normally borrow the hotbar slot the satchel overlays, which routes the item into the main inventory
    // (in the player's own InventoryMenu) or into the open container. But if the main inventory is full, the
    // hotbar route has nowhere to go, so we instead borrow a main-inventory slot, which routes the item into
    // the hotbar. All of this happens within one synchronous doClick, so no flicker or desync is ever observed.
    @WrapOperation(
            method = "doClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;quickMoveStack(Lnet/minecraft/world/entity/player/Player;I)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private ItemStack satchels$quickMoveFromSatchel(AbstractContainerMenu menu, Player player, int index, Operation<ItemStack> original) {
        Slot slot = this.slots.get(index);
        if(!(slot instanceof SatchelInventorySlot satchelSlot)) return original.call(menu, player, index);

        SatchelData data = SatchelData.get(player);
        if(!data.canAccess()) return ItemStack.EMPTY;

        int satchelIndex = satchelSlot.getContainerSlot();
        SatchelInventory satchelInv = data.getSatchelInventory();
        ItemStack satchelItem = satchelInv.getItem(satchelIndex);
        if(satchelItem.isEmpty()) return ItemStack.EMPTY;

        Inventory inventory = player.getInventory();

        // Pick the real inventory slot to borrow: the overlaid hotbar slot normally, or a main-inventory slot
        // when the main inventory can't accept the item (so it routes into the hotbar instead).
        int borrowMenuIdx = -1;
        if(satchels$mainInventoryFull(inventory, satchelItem)) {
            borrowMenuIdx = satchels$findMainInventoryMenuIndex(player);
        }
        if(borrowMenuIdx == -1) {
            borrowMenuIdx = satchels$findInventoryMenuIndex(player, satchelIndex + data.getHotbarOffset());
        }
        if(borrowMenuIdx == -1) return original.call(menu, player, index);

        int borrowInvIndex = this.slots.get(borrowMenuIdx).getContainerSlot();
        ItemStack savedItem = inventory.getItem(borrowInvIndex);

        // Present the satchel item in the borrowed slot (by reference; moveItemStackTo shrinks it in place).
        inventory.setItem(borrowInvIndex, satchelItem);
        satchelInv.setItem(satchelIndex, ItemStack.EMPTY);

        ItemStack result = original.call(menu, player, borrowMenuIdx);

        // Copy the remainder back into the satchel and restore the borrowed slot's item.
        satchelInv.setItem(satchelIndex, inventory.getItem(borrowInvIndex));
        inventory.setItem(borrowInvIndex, savedItem);

        return result;
    }

    @Unique
    private int satchels$findInventoryMenuIndex(Player player, int invIndex) {
        for(int j = 0; j < this.slots.size(); j++) {
            Slot s = this.slots.get(j);
            if(s.container == player.getInventory() && s.getContainerSlot() == invIndex) return j;
        }
        return -1;
    }

    // Any occupied main-inventory slot (Inventory indices 9-35) present in this menu; -1 if none.
    @Unique
    private int satchels$findMainInventoryMenuIndex(Player player) {
        for(int j = 0; j < this.slots.size(); j++) {
            Slot s = this.slots.get(j);
            if(s.container == player.getInventory()) {
                int cs = s.getContainerSlot();
                if(cs >= 9 && cs < 36) return j;
            }
        }
        return -1;
    }

    // True when the main inventory (Inventory indices 9-35) has no room for the item — neither an empty slot
    // nor a stackable partial slot of the same item.
    @Unique
    private boolean satchels$mainInventoryFull(Inventory inventory, ItemStack item) {
        for(int i = 9; i < 36; i++) {
            ItemStack s = inventory.getItem(i);
            if(s.isEmpty()) return false;
            if(ItemStack.isSameItemSameComponents(s, item) && s.getCount() < s.getMaxStackSize()) return false;
        }
        return true;
    }

    @Definition(id = "SWAP", field = "Lnet/minecraft/world/inventory/ClickType;SWAP:Lnet/minecraft/world/inventory/ClickType;")
    @Expression("? == SWAP")
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getItem(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 0), slice = @Slice(from = @At("MIXINEXTRAS:EXPRESSION")))
    private ItemStack satchels$getFromSatchelHotbar(Inventory instance, int slotIndex, Operation<ItemStack> original, int p_150431_, int p_150432_, ClickType p_150433_, Player player) {
        SatchelData satchelData = SatchelData.get(player);
        Slot slot = this.slots.get(slotIndex);
        return slot instanceof SatchelInventorySlot ?
                satchelData.getSatchelInventory().getItem(slot.getContainerSlot()) :
                original.call(instance, slotIndex);
    }

    @Definition(id = "SWAP", field = "Lnet/minecraft/world/inventory/ClickType;SWAP:Lnet/minecraft/world/inventory/ClickType;")
    @Expression("? == SWAP")
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setItem(ILnet/minecraft/world/item/ItemStack;)V"), slice = @Slice(from = @At("MIXINEXTRAS:EXPRESSION")))
    private void satchels$setToSatchelHotbar(Inventory instance, int slotIndex, ItemStack stack, Operation<Void> original, int p_150431_, int p_150432_, ClickType p_150433_, Player player) {
        SatchelData satchelData = SatchelData.get(player);
        Slot slot = this.slots.get(slotIndex);
        if(slot instanceof SatchelInventorySlot) satchelData.getSatchelInventory().setItem(slot.getContainerSlot(), stack);
        else original.call(instance, slotIndex, stack);
    }
}
