package net.vercte.satchels.satchel;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vercte.satchels.ModItems;
import org.jetbrains.annotations.NotNull;

public class SatchelItem extends Item {
    public SatchelItem() {
        super(new Properties().stacksTo(1));
    }

    @NotNull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        SatchelData satchelData = SatchelData.get(player);
        ItemStack heldStack = player.getItemInHand(interactionHand);
        if(!satchelData.getSatchelSlotInventory().isEmpty()) return InteractionResultHolder.fail(heldStack);
        if(!heldStack.is(ModItems.SATCHEL.get())) return InteractionResultHolder.fail(heldStack);

        if (!level.isClientSide()) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        ItemStack equippedStack = player.isCreative() ? heldStack.copy() : heldStack.copyAndClear();
        satchelData.getSatchelSlotInventory().setItem(0, equippedStack);
        return InteractionResultHolder.sidedSuccess(heldStack, level.isClientSide());
    }
}
