package net.vercte.satchels.compat.vanilla;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.vercte.satchels.ModAttachmentTypes;

public class SatchelEquipmentSlot extends SlotItemHandler {
    private final Player player;

    public SatchelEquipmentSlot(Player player, SatchelSlotItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public void setChanged() {
        this.player.syncData(ModAttachmentTypes.SATCHEL_SLOT);
    }
}
