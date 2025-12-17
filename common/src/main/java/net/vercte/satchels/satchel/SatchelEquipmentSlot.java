package net.vercte.satchels.satchel;

import net.minecraft.sounds.SoundEvents;
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

    @Override
    public void setByPlayer(ItemStack itemStack) {
        if(!itemStack.isEmpty()) {
            Player player = ((SatchelSlotInventory)this.container).getSatchelData().getPlayer();
            player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER.value(), 1.0f, 0.9f + player.getRandom().nextFloat()/5);
        }

        this.setByPlayer(itemStack, this.getItem());
    }
}
