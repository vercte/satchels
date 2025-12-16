package net.vercte.satchels.satchel;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.vercte.satchels.ModItems;
import net.vercte.satchels.network.SatchelStatusPacketS2C;
import org.jetbrains.annotations.NotNull;

public class SatchelData {
    private final Player player;
    private final SatchelInventory satchelInventory;
    private final SatchelSlotInventory satchelSlotInventory;

    boolean satchelEnabled;
    int timesChanged;

    public SatchelData(Player player) {
        this.player = player;
        this.satchelEnabled = false;
        this.timesChanged = 0;

        this.satchelInventory = new SatchelInventory(this);
        this.satchelSlotInventory = new SatchelSlotInventory(this);
    }

    public void save(CompoundTag tag) {
        CompoundTag satchelDataTag = new CompoundTag();

        satchelDataTag.putBoolean("satchelEnabled", this.satchelEnabled);
        satchelDataTag.put("satchelInventory", this.satchelInventory.save(new ListTag()));
        satchelDataTag.put("satchelSlot", this.satchelSlotInventory.save(new CompoundTag()));

        tag.put("satchels$satchelData", satchelDataTag);
    }

    public void load(CompoundTag tag) {
        CompoundTag satchelDataTag = tag.getCompound("satchels$satchelData");

        this.satchelEnabled = satchelDataTag.getBoolean("satchelEnabled");

        ListTag inventoryTag = satchelDataTag.getList("satchelInventory", ListTag.TAG_COMPOUND);
        this.satchelInventory.load(inventoryTag);

        CompoundTag satchelSlotTag = satchelDataTag.getCompound("satchelSlot");
        this.satchelSlotInventory.load(satchelSlotTag);
    }

    public void updateClient() {
        if(!(this.player instanceof ServerPlayer sp)) return;
        SatchelStatusPacketS2C.send(sp, this.satchelEnabled);
    }

    @NotNull
    public static SatchelData get(Player player) {
        return ((HasSatchelData)player).satchels$getSatchelData();
    }

    public boolean canAccessSatchelInventory() {
        return this.getSatchelSlotInventory().getItem(0).is(ModItems.SATCHEL.get());
    }

    public void setSatchelEnabled(boolean enabled) {
        this.satchelEnabled = enabled;
    }

    public boolean isSatchelEnabled() {
        return canAccessSatchelInventory() && this.satchelEnabled;
    }

    public boolean isSlotInSatchel(int slot) {
        return 0 <= slot && slot < 6;
    }

    public Slot getSatchelMenuSlot(int i) {
        NonNullList<Slot> slots = this.player.inventoryMenu.slots;

        for(Slot slot : slots) {
            if(slot instanceof SatchelInventorySlot && slot.getContainerSlot() == i) return slot;
        }
        return null;
    }

    // TODO: Configure satchel position
    public int convertToSatchelIndex(int slot) {
        return slot;
    }

    public void setChanged() { this.timesChanged += 1; }
    public int getTimesChanged() { return this.timesChanged; }

    public Player getPlayer() { return player; }
    public SatchelInventory getSatchelInventory() { return satchelInventory; }
    public SatchelSlotInventory getSatchelSlotInventory() { return satchelSlotInventory; }
}
