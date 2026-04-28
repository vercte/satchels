package net.vercte.satchels.api;

import net.minecraft.world.inventory.Slot;
import net.vercte.satchels.content.satchel.SatchelData;
import net.vercte.satchels.content.satchel.SatchelInventorySlot;

import java.util.function.Consumer;

/**
 * <p>A class that contains utilities for adding the Satchel's slots to a menu.</p>
 */
public class MenuWithSatchel {
    /**
     * Add the satchel's slots to a menu.
     * @param satchelData The <code>SatchelData</code> that the slots will reference.
     * @param consumer A consumer that accepts a <code>Slot</code> (e.x. <code>AbstractContainerMenu::addSlot</code>).
     * @param x The position of the left edge of the slot group.
     * @param y The position of the top edge of the slot group.
     * @param padding The padding between the slots.
     */
    public static void addInventorySlots(SatchelData satchelData, Consumer<Slot> consumer, int x, int y, int padding) {
        for(int i = 0; i < satchelData.getSatchelInventory().getContainerSize(); i++) {
            int xPos = x + i * padding;
            SatchelInventorySlot slot = new SatchelInventorySlot(satchelData.getSatchelInventory(), i, xPos, y);
            slot.updateX();
            consumer.accept(slot);
        }
    }
}