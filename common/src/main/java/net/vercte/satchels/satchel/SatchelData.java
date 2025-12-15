package net.vercte.satchels.satchel;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.vercte.satchels.ModItems;
import org.jetbrains.annotations.NotNull;

public class SatchelData {
    private final Player player;
    private final SatchelInventory satchelInventory;
    private final SatchelSlotInventory satchelSlotInventory;

    int timesChanged;

    public SatchelData(Player player) {
        this.player = player;
        this.timesChanged = 0;

        this.satchelInventory = new SatchelInventory(this);
        this.satchelSlotInventory = new SatchelSlotInventory(this);
    }

    public void save(CompoundTag tag) {
        CompoundTag satchelDataTag = new CompoundTag();

        satchelDataTag.put("satchelInventory", this.satchelInventory.save(new ListTag()));
        satchelDataTag.put("satchelSlot", this.satchelSlotInventory.save(new CompoundTag()));

        tag.put("satchels$satchelData", satchelDataTag);
    }

    public void load(CompoundTag tag) {
        CompoundTag satchelDataTag = tag.getCompound("satchels$satchelData");

        ListTag inventoryTag = satchelDataTag.getList("satchelInventory", ListTag.TAG_COMPOUND);
        this.satchelInventory.load(inventoryTag);

        CompoundTag satchelSlotTag = satchelDataTag.getCompound("satchelSlot");
        this.satchelSlotInventory.load(satchelSlotTag);
    }

    @NotNull
    public static SatchelData get(Player player) {
        return ((HasSatchelData)player).satchels$getSatchelData();
    }

    public boolean canAccessSatchelInventory() {
        return this.getSatchelSlotInventory().getItem(0).is(ModItems.SATCHEL.get());
    }

    public void setChanged() { this.timesChanged += 1; }
    public int getTimesChanged() { return this.timesChanged; }

    public Player getPlayer() { return player; }
    public SatchelInventory getSatchelInventory() { return satchelInventory; }
    public SatchelSlotInventory getSatchelSlotInventory() { return satchelSlotInventory; }
}
