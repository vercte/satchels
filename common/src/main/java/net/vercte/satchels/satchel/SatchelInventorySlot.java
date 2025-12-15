package net.vercte.satchels.satchel;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SatchelInventorySlot extends Slot {
    public SatchelInventorySlot(SatchelInventory inventory, int slot, int x, int y) {
        super(inventory, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return ((SatchelInventory)this.container).isAccessible();
    }

    public boolean allowModification(Player player) {
        return super.allowModification(player) && ((SatchelInventory)this.container).isAccessible();
    }
}
