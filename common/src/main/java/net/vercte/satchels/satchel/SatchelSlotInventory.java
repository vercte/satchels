package net.vercte.satchels.satchel;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SatchelSlotInventory implements Container {
    private final SatchelData satchelData;
    private ItemStack stack;

    public SatchelSlotInventory(SatchelData parent) {
        this.stack = ItemStack.EMPTY;
        this.satchelData = parent;
    }

    public Tag save(CompoundTag tag) {
        if(this.stack.isEmpty()) return tag;
        return this.stack.save(this.satchelData.getPlayer().registryAccess(), tag);
    }

    public void load(CompoundTag tag) {
        this.stack = ItemStack.parse(this.satchelData.getPlayer().registryAccess(), tag).orElse(ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() { return 1; }

    @Override
    public boolean isEmpty() { return this.stack.isEmpty(); }

    @Override
    @NotNull
    public ItemStack getItem(int slot) { return this.stack; }

    @Override
    @NotNull
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(List.of(this.stack), slot, amount);
    }

    @Override
    @NotNull
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack s = this.stack;
        this.stack = ItemStack.EMPTY;
        return s;
    }

    @Override
    public void setItem(int slot, ItemStack newStack) {
        this.stack = newStack;
    }

    @Override
    public void setChanged() {
        this.satchelData.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return player.canInteractWithEntity(this.satchelData.getPlayer(), 4.0);
    }

    @Override
    public void clearContent() {
        this.stack = ItemStack.EMPTY;
    }
}
