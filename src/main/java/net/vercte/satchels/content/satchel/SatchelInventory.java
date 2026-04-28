package net.vercte.satchels.content.satchel;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SatchelInventory implements Container, INBTSerializable<CompoundTag>, StackedContentsCompatible {
    private final String KEY_ITEMS = "Items";
    private final String KEY_SLOT = "Slot";
    private final int SATCHEL_SIZE = 6;

    private final SatchelData parent;
    private final List<ItemStack> items;

    public SatchelInventory(SatchelData parent) {
        this.parent = parent;
        this.items = NonNullList.withSize(SATCHEL_SIZE, ItemStack.EMPTY);
    }

    public SatchelInventory copy(SatchelData parent) {
        SatchelInventory copied = new SatchelInventory(parent);
        for(int i = 0; i < this.items.size(); i++) {
            copied.setItem(i, this.items.get(i).copy());
        }

        return copied;
    }

    // region Container
    @Override
    public int getContainerSize() { return SATCHEL_SIZE; }

    @Override
    @NotNull
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        items.set(slot, stack);
    }

    @Override
    @NotNull
    public ItemStack removeItem(int slot, int amount) {
        return !this.items.get(slot).isEmpty() ? ContainerHelper.removeItem(items, slot, amount) : ItemStack.EMPTY;
    }

    @Override
    @NotNull
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack removed = this.items.get(slot);
        this.items.set(slot, ItemStack.EMPTY);
        return removed;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public void setChanged() {
        this.parent.getPlayer().getInventory().setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return player.canInteractWithEntity(this.parent.getPlayer(), 4.0F);
    }
    // endregion Container

    // region Inventory Parity
    public void removeItem(ItemStack item) {
        for(int i = 0; i < this.items.size(); i++) {
            if(this.items.get(i) == item) {
                this.items.set(i, ItemStack.EMPTY);
            }
        }
    }

    public boolean pickup(ItemStack stack) {
        Inventory inventory = this.parent.getPlayer().getInventory();

        int offset = parent.getHotbarOffset();
        int right = parent.getHotbarOffset() + 6;

        List<ItemStack> kindaHotbar = new ArrayList<>();
        for(int i = 0; i < offset; i++) kindaHotbar.add(inventory.getItem(i)); // uncovered left side of hotbar
        kindaHotbar.addAll(this.items);
        for(int i = right; i < 9; i++) kindaHotbar.add(inventory.getItem(i)); // uncovered right side of hotbar

        boolean success = addToInventory(stack, kindaHotbar);

        for(int i = 0; i < offset; i++) inventory.items.set(i, kindaHotbar.get(i)); // uncovered left side of hotbar
        for(int i = offset; i < right; i++) this.items.set(i-offset, kindaHotbar.get(i));
        for(int i = right; i < 9; i++) inventory.items.set(i, kindaHotbar.get(i));

        return success;
    }

    public boolean addToInventory(ItemStack ins, List<ItemStack> items) {
        int availableSlot = getSlotWithRemainingSpace(ins, items);
        if(availableSlot != -1) {
            addAt(availableSlot, ins, items);
        } else {
            for(int i = 0; i < items.size(); i++) {
                addAt(i, ins, items);
                if(ins.getCount() == 0) return true;
            }
            return false;
        }
        if(ins.getCount() > 0) return addToInventory(ins, items);
        return true;
    }

    public void addAt(int slot, ItemStack ins, List<ItemStack> items) {
        ItemStack original = items.get(slot);
        if(original.isEmpty()) {
            items.set(slot, ins.copyAndClear());
            items.get(slot).setPopTime(5);
        } else if(stackCanFitMore(original, ins)) {
            int inserted = Math.min(original.getMaxStackSize() - original.getCount(), ins.getCount());
            ins.shrink(inserted);

            original.setCount(original.getCount() + inserted);
            original.setPopTime(5);
        }
    }

    public void dropAll(boolean died) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack itemStack = items.get(i);
            if (!itemStack.isEmpty()) {
                this.parent.getPlayer().drop(itemStack, died, !died);
                items.set(i, ItemStack.EMPTY);
            }
        }
    }

    public boolean placeItemBackInInventory(ItemStack inserted) {
        while(!inserted.isEmpty()) {
            int slot = this.getSlotWithRemainingSpace(inserted, items);
            if (slot == -1) {
                slot = this.getFreeSlot();
            }

            if (slot == -1) break;

            int j = inserted.getMaxStackSize() - this.getItem(slot).getCount();
            this.addAt(slot, inserted.split(j), items);
        }
        return inserted.isEmpty();
    }

    public int getSlotWithRemainingSpace(ItemStack inserted, List<ItemStack> items) {
        int selected = getSelectedSlot();
        if(selected != -1 && stackCanFitMore(items.get(selected), inserted)) return selected;

        for(int i = 0; i < items.size(); i++) {
            ItemStack here = items.get(i);
            if(this.stackCanFitMore(here, inserted)) return i;
        }
        return -1;
    }

    public boolean stackCanFitMore(ItemStack original, ItemStack inserted) {
        return !original.isEmpty() &&
                ItemStack.isSameItemSameComponents(original, inserted) &&
                original.isStackable() &&
                original.getCount() < this.getMaxStackSize(original);
    }

    public int getFreeSlot() {
        for(int i = 0; i < this.items.size(); i++) {
            if(this.items.get(i).isEmpty()) return i;
        }
        return -1;
    }

    private int getSelectedSlot() {
        int invSelected = this.parent.getPlayer().getInventory().selected;
        if(this.parent.isSlotInSatchel(invSelected)) return this.parent.convertToSatchelIndex(invSelected);
        return -1;
    }

    public int findSlotMatchingUnusedItem(ItemStack searchingFor) {
        for (int i = 0; i < this.items.size(); i++) {
            ItemStack found = this.items.get(i);
            if (!found.isEmpty()
                    && ItemStack.isSameItemSameComponents(searchingFor, found)
                    && !found.isDamaged()
                    && !found.isEnchanted()
                    && !found.has(DataComponents.CUSTOM_NAME)) {
                return i;
            }
        }

        return -1;
    }

    public int findSlotMatchingItem(ItemStack searchingFor) {
        for (int i = 0; i < this.items.size(); i++) {
            ItemStack found = this.items.get(i);
            if (!found.isEmpty() && ItemStack.isSameItemSameComponents(searchingFor, found)) {
                return i;
            }
        }

        return -1;
    }
    // endregion

    // region Serialization
    @Override
    public CompoundTag serializeNBT(@NotNull HolderLookup.Provider provider) {
        ListTag listTag = new ListTag();

        for(int i = 0; i < this.items.size(); i++) {
            ItemStack slotContent = this.items.get(i);
            if (!slotContent.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt(KEY_SLOT, i);
                listTag.add(slotContent.save(provider, itemTag));
            }
        }

        CompoundTag tag = new CompoundTag();
        tag.put(KEY_ITEMS, listTag);
        return tag;
    }

    @Override
    public void deserializeNBT(@NotNull HolderLookup.Provider provider, @NotNull CompoundTag tag) {
        ListTag tagList = tag.getList(KEY_ITEMS, ListTag.TAG_COMPOUND);

        for(int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt(KEY_SLOT);
            if (slot >= 0 && slot < this.items.size()) {
                ItemStack.parse(provider, itemTags).ifPresent((stack) -> this.items.set(slot, stack));
            }
        }
    }

    public void serializeIntoByteBuf(RegistryFriendlyByteBuf byteBuf) {
        ItemStack.OPTIONAL_LIST_STREAM_CODEC.encode(byteBuf, this.items);
    }

    public void deserializeFromByteBuf(RegistryFriendlyByteBuf byteBuf) {
        List<ItemStack> newItems = ItemStack.OPTIONAL_LIST_STREAM_CODEC.decode(byteBuf);
        for(int i = 0; i < this.items.size(); i++) {
            this.items.set(i, newItems.get(i));
        }
    }
    // endregion

    // region StackedContentsCompatible
    @Override
    public void fillStackedContents(@NotNull StackedContents contents) {
        for (ItemStack itemstack : this.items) {
            contents.accountSimpleStack(itemstack);
        }
    }
    // endregion

    public SatchelData getParent() {
        return parent;
    }
    public List<ItemStack> getItems() { return items; }
}
