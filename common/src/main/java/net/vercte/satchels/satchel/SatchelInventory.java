package net.vercte.satchels.satchel;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SatchelInventory implements Container {
    public final NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
    public final SatchelData satchelData;

    public SatchelInventory(SatchelData parent) {
        this.satchelData = parent;
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

    public void dropAll(boolean died) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack itemStack = items.get(i);
            if (!itemStack.isEmpty()) {
                this.satchelData.getPlayer().drop(itemStack, died, !died);
                items.set(i, ItemStack.EMPTY);
            }
        }
    }

    // EVERYTHING BELOW HERE IS TAKEN FROM Inventory
    public boolean placeItemBackInInventory(ItemStack stack) {
        while(!stack.isEmpty()) {
            int notQuiteFullStackIndex = this.getSlotWithRemainingSpace(stack);
            if (notQuiteFullStackIndex == -1) {
                notQuiteFullStackIndex = this.getFreeSlot();
            }

            if (notQuiteFullStackIndex == -1) break;

            int j = stack.getMaxStackSize() - this.getItem(notQuiteFullStackIndex).getCount();
            this.add(notQuiteFullStackIndex, stack.split(j));
        }
        return stack.isEmpty();
    }

    public int getFreeSlot() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (this.getItem(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    public int getSlotWithRemainingSpace(ItemStack itemStack) {
        Inventory playerInventory = this.satchelData.getPlayer().getInventory();
        int selected = playerInventory.selected;

        if (this.satchelData.isSlotInSatchel(selected) &&  this.hasRemainingSpaceForItem(this.getItem(this.satchelData.convertToSatchelIndex(selected)), itemStack)) {
            return this.satchelData.convertToSatchelIndex(selected);
        } else {
            for (int i = 0; i < this.items.size(); i++) {
                if (this.hasRemainingSpaceForItem(this.getItem(i), itemStack)) {
                    return i;
                }
            }

            return -1;
        }
    }

    private boolean hasRemainingSpaceForItem(ItemStack itemStack, ItemStack itemStack2) {
        return !itemStack.isEmpty()
                && ItemStack.isSameItemSameComponents(itemStack, itemStack2)
                && itemStack.isStackable()
                && itemStack.getCount() < this.getMaxStackSize(itemStack);
    }

    public boolean add(ItemStack stack) {
        return this.add(-1, stack);
    }

    public boolean add(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        } else {
            Player player = this.satchelData.getPlayer();
            try {
                if (stack.isDamaged()) {
                    if (slot == -1) {
                        slot = this.getFreeSlot();
                    }

                    if (slot >= 0) {
                        this.items.set(slot, stack.copyAndClear());
                        this.items.get(slot).setPopTime(5);
                        return true;
                    } else if (player.hasInfiniteMaterials()) {
                        stack.setCount(0);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    int j;
                    do {
                        j = stack.getCount();
                        if (slot == -1) {
                            stack.setCount(this.addResource(stack));
                        } else {
                            stack.setCount(this.addResource(slot, stack));
                        }
                    } while (!stack.isEmpty() && stack.getCount() < j);

                    if (stack.getCount() == j && player.hasInfiniteMaterials()) {
                        stack.setCount(0);
                        return true;
                    } else {
                        return stack.getCount() < j;
                    }
                }
            } catch (Throwable var6) {
                CrashReport crashReport = CrashReport.forThrowable(var6, "Adding item to satchel inventory");
                CrashReportCategory crashReportCategory = crashReport.addCategory("Item being added");
                crashReportCategory.setDetail("Item ID", Item.getId(stack.getItem()));
                crashReportCategory.setDetail("Item data", stack.getDamageValue());
                crashReportCategory.setDetail("Item name", () -> stack.getHoverName().getString());
                throw new ReportedException(crashReport);
            }
        }
    }

    private int addResource(ItemStack itemStack) {
        int slot = this.getSlotWithRemainingSpace(itemStack);
        if (slot == -1) {
            slot = this.getFreeSlot();
        }

        return slot == -1 ? itemStack.getCount() : this.addResource(slot, itemStack);
    }

    private int addResource(int i, ItemStack itemStack) {
        int count = itemStack.getCount();
        ItemStack containedItem = this.getItem(i);
        if (containedItem.isEmpty()) {
            containedItem = itemStack.copyWithCount(0);
            this.setItem(i, containedItem);
        }

        int max = this.getMaxStackSize(containedItem) - containedItem.getCount();
        int added = Math.min(count, max);
        if (added != 0) {
            count -= added;
            containedItem.grow(added);
            containedItem.setPopTime(5);
        }
        return count;
    }
}
