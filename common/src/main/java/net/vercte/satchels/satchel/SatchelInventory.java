package net.vercte.satchels.satchel;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SatchelInventory implements Container {
    public final NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
    public final SatchelData satchelData;

    public SatchelInventory(SatchelData parent) {
        this.satchelData = parent;
    }

    public boolean isAccessible() {
        return this.satchelData.canAccessSatchelInventory();
    }

    public ListTag save(ListTag tag) {
        for(int i = 0; i < this.items.size(); i++) {
            if(this.items.get(i).isEmpty()) continue;

            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putByte("Slot", (byte)i);
            tag.add(this.items.get(i).save(this.satchelData.getPlayer().registryAccess(), compoundTag));
        }

        return tag;
    }

    public void load(ListTag tag) {
        this.items.clear();

        for (int i = 0; i < tag.size(); i++) {
            CompoundTag compoundTag = tag.getCompound(i);

            int slot = compoundTag.getByte("Slot");
            ItemStack stack = ItemStack.parse(this.satchelData.getPlayer().registryAccess(), compoundTag).orElse(ItemStack.EMPTY);
            this.items.set(slot, stack);
        }
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

    public void dropAll() {
        for (int i = 0; i < items.size(); i++) {
            ItemStack itemStack = items.get(i);
            if (!itemStack.isEmpty()) {
                this.satchelData.getPlayer().drop(itemStack, true, false);
                items.set(i, ItemStack.EMPTY);
            }
        }
    }
}
