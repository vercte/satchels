package net.vercte.satchels.satchel;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SatchelInventory implements Container {
    public final NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
    public final SatchelData satchelData;

    public SatchelInventory(SatchelData parent) {
        this.satchelData = parent;
    }

    public boolean isAccessible() {
        return this.satchelData.canAccessSatchelInventory();
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    @NotNull
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    @NotNull
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(items, slot, amount);
    }

    @Override
    @NotNull
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = items.get(slot);
        items.set(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
    }

    @Override
    public void setChanged() { this.satchelData.setChanged(); }

    @Override
    public boolean stillValid(Player player) {
        return player.canInteractWithEntity(this.satchelData.getPlayer(), 4.0);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }
}
