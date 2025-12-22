package net.vercte.satchels.fabric.compat.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.ModTags;
import net.vercte.satchels.satchel.SatchelData;

public class SatchelTrinket implements Trinket {
    @Override
    public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(!(entity instanceof Player player)) return false;
        return SatchelData.get(player).getSatchelInventory().isEmpty();
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        ItemStack to = slot.inventory().getItem(slot.index());
        if(!stack.is(ModTags.SATCHEL)) return;
        if(to.is(ModTags.SATCHEL)) return;

        if(!(entity instanceof Player player)) return;

        SatchelData satchelData = SatchelData.get(player);
        satchelData.getSatchelInventory().dropAll(false);
        satchelData.setSatchelEnabled(false, true);
        if(!entity.level().isClientSide()) satchelData.updateClient();
    }
}
