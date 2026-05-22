package net.vercte.satchels.compat.vanilla;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.fml.ModList;
import net.vercte.satchels.ModAttachmentTypes;
import net.vercte.satchels.ModTags;
import net.vercte.satchels.api.SatchelAccess;
import net.vercte.satchels.compat.CompatEntrypoint;
import net.vercte.satchels.content.satchel.SatchelItem;

import java.util.Optional;

public class VanillaCompat implements CompatEntrypoint {
    @Override
    public void initialize() {
        SatchelAccess.CAN_ACCESS_PREDICATES.add(this::canAccessSatchel);
        SatchelAccess.IS_VISIBLE_PREDICATES.add(this::isSatchelVisible);
        SatchelAccess.SATCHEL_STACK_GETTERS.add(this::getSatchel);
        SatchelAccess.SATCHEL_TINT_GETTERS.add(this::getSatchelTint);
    }

    private ItemStack getSatchel(Player player) {
        return player.getData(ModAttachmentTypes.SATCHEL_SLOT).getStackInSlot(0);
    }

    private boolean canAccessSatchel(Player player) {
        return getSatchel(player).is(ModTags.SATCHEL);
    }

    private int getSatchelTint(Player player) {
        return DyedItemColor.getOrDefault(getSatchel(player), SatchelItem.DEFAULT_COLOR);
    }

    private boolean isSatchelVisible(Player player) {
        return !getSatchel(player).isEmpty();
    }

    public static boolean shouldBeLoaded(ModList list) {
        return !list.isLoaded("curios");
    }
}
