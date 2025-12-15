package net.vercte.satchels.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.vercte.satchels.satchel.HasSatchelData;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin implements HasSatchelData {
    @Unique private SatchelData satchels$satchelData;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/InventoryMenu;<init>(Lnet/minecraft/world/entity/player/Inventory;ZLnet/minecraft/world/entity/player/Player;)V"))
    public void addSatchelInventory(Level level, BlockPos blockPos, float f, GameProfile gameProfile, CallbackInfo ci) {
        this.satchels$satchelData = new SatchelData((Player)(Object) this);
    }

    @Override
    public SatchelData satchels$getSatchelData() {
        return this.satchels$satchelData;
    }
}
