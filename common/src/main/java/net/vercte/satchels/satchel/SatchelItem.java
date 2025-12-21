package net.vercte.satchels.satchel;

import net.minecraft.world.item.Item;

public class SatchelItem extends Item {
    public SatchelItem() {
        super(new Properties().stacksTo(1));
    }

// TODO: use slot apis

//    @NotNull
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
//        SatchelData satchelData = SatchelData.get(player);
//        ItemStack heldStack = player.getItemInHand(interactionHand);
//        if(!satchelData.getSatchelSlotInventory().isEmpty()) return InteractionResultHolder.fail(heldStack);
//        if(!heldStack.is(ModItems.SATCHEL.get())) return InteractionResultHolder.fail(heldStack);
//
//        if (!level.isClientSide()) {
//            player.awardStat(Stats.ITEM_USED.get(this));
//        }
//
//        ItemStack equippedStack = player.isCreative() ? heldStack.copy() : heldStack.copyAndClear();
//        satchelData.getSatchelSlotInventory().setItem(0, equippedStack);
//        return InteractionResultHolder.sidedSuccess(heldStack, level.isClientSide());
//    }
}
