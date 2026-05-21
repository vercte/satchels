package net.vercte.satchels.compat.vanilla;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.vercte.satchels.ModTags;
import org.jetbrains.annotations.NotNull;

public class SatchelSlotItemStackHandler extends ItemStackHandler {
    public SatchelSlotItemStackHandler() {
        super();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.is(ModTags.SATCHEL);
    }
}
