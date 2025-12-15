package net.vercte.satchels.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.vercte.satchels.satchel.HasSatchelData;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export=true)
@Mixin(Player.class)
public class PlayerMixin implements HasSatchelData {
    @Unique private SatchelData satchels$satchelData;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/InventoryMenu;<init>(Lnet/minecraft/world/entity/player/Inventory;ZLnet/minecraft/world/entity/player/Player;)V"))
    public void addSatchelInventory(Level level, BlockPos blockPos, float f, GameProfile gameProfile, CallbackInfo ci) {
        this.satchels$satchelData = new SatchelData((Player)(Object) this);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalData(CompoundTag compoundTag, CallbackInfo ci) {
        satchels$satchelData.save(compoundTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalData(CompoundTag compoundTag, CallbackInfo ci) {
        satchels$satchelData.load(compoundTag);
    }

    @Override
    public SatchelData satchels$getSatchelData() {
        return this.satchels$satchelData;
    }
}
