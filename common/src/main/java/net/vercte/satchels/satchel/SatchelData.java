package net.vercte.satchels.satchel;

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
