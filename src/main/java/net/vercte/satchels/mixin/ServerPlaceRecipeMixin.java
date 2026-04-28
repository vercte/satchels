package net.vercte.satchels.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.vercte.satchels.content.satchel.SatchelData;
import net.vercte.satchels.content.satchel.SatchelInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlaceRecipe.class)
public class ServerPlaceRecipeMixin {
    @Shadow
    @Final
    protected StackedContents stackedContents;

    @Shadow
    protected Inventory inventory;

    @Inject(method = "recipeClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/RecipeBookMenu;fillCraftSlotsStackedContents(Lnet/minecraft/world/entity/player/StackedContents;)V", shift = At.Shift.AFTER))
    public void placeWithSatchelContents(ServerPlayer player, RecipeHolder<?> holder, boolean idgaf, CallbackInfo ci) {
        SatchelData.get(player).getSatchelInventory().fillStackedContents(stackedContents);
    }

    @ModifyReturnValue(method = "moveItemToGrid", at = @At(value = "RETURN", ordinal = 0))
    protected int searchSatchel(int original, Slot slot, ItemStack stack, int amount) {
        SatchelData data = SatchelData.get(this.inventory.player);
        SatchelInventory satchelInventory = data.getSatchelInventory();
        int s = satchelInventory.findSlotMatchingUnusedItem(stack);
        if(s == -1) {
            return -1;
        }

        ItemStack found = data.getSatchelInventory().getItem(s);
        int moved;
        if (amount < found.getCount()) {
            satchelInventory.removeItem(s, amount);
            moved = amount;
        } else {
            satchelInventory.removeItemNoUpdate(s);
            moved = found.getCount();
        }

        if (slot.getItem().isEmpty()) {
            slot.set(found.copyWithCount(moved));
        } else {
            slot.getItem().grow(moved);
        }

        return amount - moved;
    }
}
