package net.vercte.satchels.content.satchel;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import net.vercte.satchels.ModSounds;
import net.vercte.satchels.api.SatchelAccess;
import net.vercte.satchels.client.SatchelsClientConfig;
import net.vercte.satchels.network.packets.SatchelStatusPacketS2C;
import org.jetbrains.annotations.NotNull;

public class SatchelData implements INBTSerializable<CompoundTag> {
    public static final String KEY_SATCHEL = "satchels:satchel_data";
    private static final String KEY_ACTIVE = "Active";
    private static final String KEY_INVENTORY = "Inventory";

    private final Player player;

    private final SatchelInventory satchelInventory;

    private int hotbarOffset;
    private boolean active;

    public SatchelData(Player player) {
        this.player = player;
        this.satchelInventory = new SatchelInventory(this);
        this.hotbarOffset = 0;

        if(player.isLocalPlayer()) {
            this.hotbarOffset = SatchelsClientConfig.getSatchelOffset();
        }
    }

    public static SatchelData get(Player player) {
        return ((IHaveSatchelData)player).satchels$getSatchelData();
    }

    public void sendData() {
        if(!(this.player instanceof ServerPlayer serverPlayer)) throw new AssertionError("sendData should only be called on the server");
        PacketDistributor.sendToPlayer(serverPlayer, new SatchelStatusPacketS2C(this.active));
    }

    public boolean canAccess() {
        return SatchelAccess.CAN_ACCESS_PREDICATES.stream().anyMatch(p -> p.test(player));
    }

    // region Utilities
    public boolean isSlotInSatchel(int slot) {
        int offset = this.getHotbarOffset();
        return (offset <= slot) && (slot < offset+6);
    }

    public int convertToSatchelIndex(int slot) {
        return slot - this.getHotbarOffset();
    }
    // endregion

    // region Getters & Setters
    public Player getPlayer() {
        return player;
    }

    public SatchelInventory getSatchelInventory() {
        return satchelInventory;
    }

    public int getHotbarOffset() {
        return hotbarOffset;
    }

    public void setHotbarOffset(int hotbarOffset) {
        this.hotbarOffset = hotbarOffset;

        player.inventoryMenu.slots.forEach(s -> {
            if (s instanceof SatchelInventorySlot ss) ss.updateX();
        });

        if(player.containerMenu != player.inventoryMenu) {
            player.containerMenu.slots.forEach(s -> {
                if (s instanceof SatchelInventorySlot ss) ss.updateX();
            });
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean to, boolean audible) {
        this.active = to;

        if(!audible) return;
        float pitch = 0.9f + (player.getRandom().nextFloat() / 5);

        Vec3 soundPos = player.position();
        if(to) {
            player.level().playSound(null,
                    soundPos.x, soundPos.y, soundPos.z,
                    ModSounds.SATCHEL_OPEN.get(), SoundSource.PLAYERS,
                    1, pitch
            );
        } else {
            player.level().playSound(null,
                    soundPos.x, soundPos.y, soundPos.z,
                    ModSounds.SATCHEL_CLOSE.get(), SoundSource.PLAYERS,
                    1, pitch
            );
        }
    }
    // endregion

    // region Serialization
    @Override
    public CompoundTag serializeNBT(@NotNull HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(KEY_ACTIVE, active);

        CompoundTag inventory = this.satchelInventory.serializeNBT(provider);
        tag.put(KEY_INVENTORY, inventory);

        return tag;
    }

    @Override
    public void deserializeNBT(@NotNull HolderLookup.Provider provider, @NotNull CompoundTag tag) {
        this.active = tag.getBoolean(KEY_ACTIVE);
        this.satchelInventory.deserializeNBT(provider, tag.getCompound(KEY_INVENTORY));
    }
    // endregion
}
