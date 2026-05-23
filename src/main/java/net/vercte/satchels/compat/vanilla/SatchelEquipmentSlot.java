package net.vercte.satchels.compat.vanilla;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.vercte.satchels.ModAttachmentTypes;
import net.vercte.satchels.ModTags;
import net.vercte.satchels.content.satchel.SatchelData;
import org.jetbrains.annotations.NotNull;

public class SatchelEquipmentSlot extends Slot {
    private static final Container emptyInventory = new SimpleContainer(0);
    private SatchelSlotItemStackHandler handler;
    private final Player player;

    public SatchelEquipmentSlot(Player player, int xPosition, int yPosition) {
        super(emptyInventory, 0, xPosition, yPosition);
        this.handler = new SatchelSlotItemStackHandler();
        this.player = player;
    }

    public void onLoad(Player player) {
        this.handler = player.getData(ModAttachmentTypes.SATCHEL_SLOT);
    }

    public boolean isShown(Player player, AbstractContainerMenu menu) {
        SatchelData data = SatchelData.get(player);
        return menu.getCarried().is(ModTags.SATCHEL) || (
                data.getSatchelInventory().isEmpty() &&
                this.getItem().is(ModTags.SATCHEL)
        );
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.isEmpty())
            return false;
        return handler.isItemValid(0, stack);
    }

    @Override
    @NotNull
    public ItemStack getItem() {
        return this.getItemHandler().getStackInSlot(0);
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(0, stack);
        this.setChanged();
    }

    @Override
    public void onQuickCraft(@NotNull ItemStack oldStackIn, @NotNull ItemStack newStackIn) {}

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return 1;
    }

    @Override
    public boolean mayPickup(@NotNull Player player) {
        if(!SatchelData.get(player).getSatchelInventory().isEmpty()) return false;
        return !this.getItemHandler().extractItem(0, 1, true).isEmpty();
    }

    @Override
    @NotNull
    public ItemStack remove(int amount) {
        ItemStack removed = this.getItemHandler().extractItem(0, amount, false);
        SatchelData data = SatchelData.get(player);
        if(!removed.isEmpty() && data.isActive()) data.setActive(false, true);
        setChanged();
        return removed;
    }

    @Override
    public void setChanged() {
        player.syncData(ModAttachmentTypes.SATCHEL_SLOT);
    }

    public IItemHandler getItemHandler() {
        return handler;
    }
}
