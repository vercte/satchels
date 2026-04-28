package net.vercte.satchels.content.satchel;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SatchelInventorySlot extends Slot {
    private final int baseX;
    private final SatchelData satchelData;

    public SatchelInventorySlot(SatchelInventory inventory, int slot, int x, int y) {
        super(inventory, slot, x, y);
        this.baseX = x;
        this.satchelData = inventory.getParent();
    }

    public void updateX() {
        this.x = baseX + (satchelData.getHotbarOffset() * 18);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return this.satchelData.canAccess();
    }

    public boolean allowModification(@NotNull Player player) {
        return super.allowModification(player) && this.satchelData.canAccess();
    }

    @Override
    public boolean isHighlightable() {
        return this.satchelData.canAccess();
    }

    @Override
    @NotNull
    public ItemStack getItem() {
        return super.getItem();
    }
}
