package net.vercte.satchels.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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

    @Inject(method = "dropEquipment", at = @At("TAIL"))
    public void dropSatchelEquipment(CallbackInfo ci) {
        SatchelData satchelData = SatchelData.get((Player)(Object) this);
        satchelData.getSatchelInventory().dropAll();
        satchelData.getSatchelSlotInventory().drop();
    }

    @Inject(method = "destroyVanishingCursedItems", at = @At("TAIL"))
    public void destroyVanishingCursedItems(CallbackInfo ci) {
        SatchelData satchelData = SatchelData.get((Player)(Object) this);
        for (int i = 0; i < satchelData.getSatchelInventory().getContainerSize(); i++) {
            ItemStack itemStack = satchelData.getSatchelInventory().getItem(i);
            if (!itemStack.isEmpty() && EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
                satchelData.getSatchelInventory().removeItemNoUpdate(i);
            }
        }

        ItemStack satchelStack = satchelData.getSatchelSlotInventory().getItem(0);
        if (!satchelStack.isEmpty() && EnchantmentHelper.has(satchelStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
            satchelData.getSatchelSlotInventory().removeItemNoUpdate(0);
        }
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
