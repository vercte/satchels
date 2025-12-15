package net.vercte.satchels.satchel;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.ModItems;

public class SatchelEquipmentSlot extends Slot {
    public SatchelEquipmentSlot(SatchelSlotInventory inventory, int x, int y) {
        super(inventory, 0, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.is(ModItems.SATCHEL.get());
    }

    @Override
    public boolean mayPickup(Player player) {
        return SatchelData.get(player).getSatchelInventory().isEmpty();
    }
}
