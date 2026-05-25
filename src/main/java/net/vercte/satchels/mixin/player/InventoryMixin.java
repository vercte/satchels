package net.vercte.satchels.mixin.player;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import net.vercte.satchels.ModAttachmentTypes;
import net.vercte.satchels.network.packets.ToggleSatchelPacketC2S;
import net.vercte.satchels.content.satchel.SatchelData;
import net.vercte.satchels.content.satchel.SatchelInventory;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Shadow
    @Final
    public Player player;

    @Shadow
    public int selected;

    @Shadow
    public abstract int getSuitableHotbarSlot();

    @Shadow
    @Final
    public NonNullList<ItemStack> items;

    @ModifyReturnValue(method = "getSelected", at = @At("RETURN"))
    public ItemStack satchels$getSelected(ItemStack original) {
        SatchelData satchelData = SatchelData.get(player);
        if(satchelData.isActive() && satchelData.isSlotInSatchel(selected)) {
            int satchelIndex = satchelData.convertToSatchelIndex(selected);
            return satchelData.getSatchelInventory().getItem(satchelIndex);
        }
        return original;
    }

    @ModifyReturnValue(method = "getDestroySpeed", at = @At("RETURN"))
    public float satchels$getDestroySpeed(float original, BlockState state) {
        SatchelData satchelData = SatchelData.get(player);
        if(satchelData.isSlotInSatchel(selected) && satchelData.isActive()) {
            int satchelIndex = satchelData.convertToSatchelIndex(selected);
            return satchelData.getSatchelInventory().getItem(satchelIndex).getDestroySpeed(state);
        }
        return original;
    }

    @Inject(method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At("HEAD"), cancellable = true)
    public void satchels$placeItemBackInInventory(ItemStack stack, boolean update, CallbackInfo ci) {
        SatchelData satchelData = SatchelData.get(player);
        if(satchelData.isActive() || !update) {
            boolean added = satchelData.getSatchelInventory().placeItemBackInInventory(stack);
            if(added) ci.cancel();
        }
    }

    @Inject(method = "removeFromSelected", at = @At("HEAD"), cancellable = true)
    public void satchels$removeFromSelected(boolean fullStack, CallbackInfoReturnable<ItemStack> cir) {
        SatchelData satchelData = SatchelData.get(player);
        if(satchelData.isActive() && satchelData.isSlotInSatchel(selected)) {
            int slot = satchelData.convertToSatchelIndex(selected);
            ItemStack satchelSelected = satchelData.getSatchelInventory().getItem(slot);
            if(satchelSelected.isEmpty()) cir.setReturnValue(ItemStack.EMPTY);
            else {
                cir.setReturnValue(satchelData.getSatchelInventory().removeItem(slot, fullStack ? satchelSelected.getCount() : 1));
            }
        }
    }

    @WrapOperation(method = "clearOrCountMatchingItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ContainerHelper;clearOrCountMatchingItems(Lnet/minecraft/world/Container;Ljava/util/function/Predicate;IZ)I", ordinal = 1))
    public int satchels$clearOrCountMatchingItems(Container container, Predicate<ItemStack> predicate, int i, boolean bl, Operation<Integer> original, @Local(ordinal=1) int cleared) {
        int extraCleared = original.call(container, predicate, i - cleared, bl);
        SatchelData satchelData = SatchelData.get(player);
        extraCleared += ContainerHelper.clearOrCountMatchingItems(satchelData.getSatchelInventory(), predicate, i - extraCleared - cleared, bl);

        if(player.hasData(ModAttachmentTypes.SATCHEL_SLOT)) {
            ItemStack satchelSlot = player.getData(ModAttachmentTypes.SATCHEL_SLOT).getStackInSlot(0);
            extraCleared += ContainerHelper.clearOrCountMatchingItems(satchelSlot, predicate, i - extraCleared - cleared, bl);

            player.getData(ModAttachmentTypes.SATCHEL_SLOT).setStackInSlot(0, satchelSlot);
            player.syncData(ModAttachmentTypes.SATCHEL_SLOT);
            if (satchelSlot.isEmpty()) {
                satchelData.getSatchelInventory().dropAll(false);
                if (satchelData.isActive()) {
                    satchelData.setActive(false, true);
                    satchelData.sendData();
                }
            }
        }

        return extraCleared;
    }

    @Inject(method = "removeItem(Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "TAIL"))
    public void satchels$removeItem(ItemStack stack, CallbackInfo ci) {
        SatchelData.get(player)
                .getSatchelInventory()
                .removeItem(stack);
    }

    @Inject(method = "pickSlot", at = @At("HEAD"), cancellable = true)
    public void satchels$putIntoSatchelIfActive(int slot, CallbackInfo ci) {
        SatchelData data = SatchelData.get(player);
        SatchelInventory satchelInventory = data.getSatchelInventory();
        if(!data.isActive()) return;

        int inventorySuitable = getSuitableHotbarSlot();
        int satchelSuitable = satchelInventory.getFreeSlot();

        if(!data.isSlotInSatchel(inventorySuitable) && (inventorySuitable < satchelSuitable + data.getHotbarOffset() || satchelSuitable == -1)) return;
        if(satchelSuitable == -1 && inventorySuitable != -1 && !data.isSlotInSatchel(inventorySuitable)) return;

        if(satchelSuitable != -1) selected = satchelSuitable + data.getHotbarOffset();
        int satchelSelected = data.convertToSatchelIndex(selected);

        ItemStack held = satchelInventory.getItem(satchelSelected);

        satchelInventory.setItem(satchelSelected, items.get(slot));
        items.set(slot, held);
        ci.cancel();
    }

    @Inject(method = "setPickedItem", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Inventory;selected:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    public void satchels$deselectSatchelIfNeeded(CallbackInfo ci) {
        SatchelData data = SatchelData.get(player);
        if(!data.isSlotInSatchel(selected)) return;

        if(!data.isActive()) return;
        data.setActive(false, true);
        PacketDistributor.sendToServer(new ToggleSatchelPacketC2S(false));
    }
}
