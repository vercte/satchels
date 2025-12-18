package net.vercte.satchels.satchel;

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
        this.satchelData = inventory.satchelData;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return this.satchelData.canAccessSatchelInventory();
    }

    public boolean allowModification(Player player) {
        return super.allowModification(player) && this.satchelData.canAccessSatchelInventory();
    }

    @Override
    public boolean isHighlightable() {
        return this.satchelData.canAccessSatchelInventory();
    }

    @Override
    @NotNull
    public ItemStack getItem() {
        this.x = baseX + (satchelData.getSatchelOffset() * 18);
        return super.getItem();
    }
}
