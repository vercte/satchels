package net.vercte.satchels.platform.services;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.ModTags;

public interface ISlotModHelper {
    default boolean isItemSatchel(ItemStack stack) {
        return stack.is(ModTags.SATCHEL);
    }

    boolean playerHasSatchel(Player player);

    default boolean playerRendersSatchel(Player player) {
        return playerHasSatchel(player);
    }
}
