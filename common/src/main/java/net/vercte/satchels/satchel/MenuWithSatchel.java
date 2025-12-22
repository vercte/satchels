package net.vercte.satchels.satchel;

import net.minecraft.world.inventory.Slot;

import java.util.function.Consumer;

public class MenuWithSatchel {
    public static void addInventorySlots(SatchelData satchelData, Consumer<Slot> consumer, int x, int y, int xSpace) {
        for(int i = 0; i < satchelData.getSatchelInventory().getContainerSize(); i++) {
            int xPos = x + (i+satchelData.getSatchelOffset())*xSpace;
            consumer.accept(new SatchelInventorySlot(satchelData.getSatchelInventory(), i, xPos, y));
        }
    }
}
