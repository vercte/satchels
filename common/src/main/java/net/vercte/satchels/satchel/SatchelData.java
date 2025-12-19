package net.vercte.satchels.satchel;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.vercte.satchels.ModItems;
import net.vercte.satchels.ModSounds;
import net.vercte.satchels.network.SatchelStatusPacketS2C;
import org.jetbrains.annotations.NotNull;

public class SatchelData {
    private final Player player;
    private final SatchelInventory satchelInventory;
    private final SatchelSlotInventory satchelSlotInventory;

    boolean satchelEnabled;
    int satchelOffset;
    int timesChanged;

    public SatchelData(Player player) {
        this.player = player;
        this.satchelEnabled = false;
        this.satchelOffset = 0;
        this.timesChanged = 0;

        this.satchelInventory = new SatchelInventory(this);
        this.satchelSlotInventory = new SatchelSlotInventory(this);
    }

    @NotNull
    public static SatchelData get(Player player) {
        return ((HasSatchelData)player).satchels$getSatchelData();
    }

    public boolean canAccessSatchelInventory() {
        return this.getSatchelSlotInventory().getItem(0).is(ModItems.SATCHEL.get());
    }

    public boolean isSatchelEnabled() {
        return canAccessSatchelInventory() && this.satchelEnabled;
    }

    public void setSatchelEnabled(boolean enabled, boolean playSound) {
        this.satchelEnabled = enabled;

        if(!playSound) return;
        float pitch = 0.9f + (player.getRandom().nextFloat() / 5);
        if (enabled) player.playSound(ModSounds.SATCHEL_OPEN.get(), 0.5f, pitch);
        else player.playSound(ModSounds.SATCHEL_CLOSE.get(), 0.5f, pitch);
    }

    public boolean isSatchelRendered() {
        return canAccessSatchelInventory();
    }

    public boolean isSlotInSatchel(int slot) {
        return getSatchelOffset() <= slot && slot < 6 + getSatchelOffset();
    }

    public int convertToSatchelIndex(int slot) {
        return slot - getSatchelOffset();
    }

    public void setSatchelOffset(int satchelOffset) {
        this.satchelOffset = Mth.clamp(0, satchelOffset, 3);
    }

    public int getSatchelOffset() {
        return this.satchelOffset;
    }

    public void updateClient() {
        if(!(this.player instanceof ServerPlayer sp)) return;
        SatchelStatusPacketS2C.send(sp, this.satchelEnabled);
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

    public void setChanged() { this.timesChanged += 1; }
    public int getTimesChanged() { return this.timesChanged; }

    public Player getPlayer() { return player; }
    public SatchelInventory getSatchelInventory() { return satchelInventory; }
    public SatchelSlotInventory getSatchelSlotInventory() { return satchelSlotInventory; }
}
