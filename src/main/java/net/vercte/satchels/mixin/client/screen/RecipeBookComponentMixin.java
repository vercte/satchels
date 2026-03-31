package net.vercte.satchels.mixin.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.entity.player.StackedContents;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookComponent.class)
public class RecipeBookComponentMixin {
    @Shadow
    protected Minecraft minecraft;

    @Shadow
    @Final
    private StackedContents stackedContents;

    @Inject(method = "initVisuals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/RecipeBookMenu;fillCraftSlotsStackedContents(Lnet/minecraft/world/entity/player/StackedContents;)V", shift = At.Shift.AFTER))
    private void fillInitContentsWithSatchel(CallbackInfo ci) {
        SatchelData.get(minecraft.player).getSatchelInventory().fillStackedContents(stackedContents);
    }

    @Inject(method = "updateStackedContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/RecipeBookMenu;fillCraftSlotsStackedContents(Lnet/minecraft/world/entity/player/StackedContents;)V", shift = At.Shift.AFTER))
    private void fillContentsWithSatchel(CallbackInfo ci) {
        SatchelData.get(minecraft.player).getSatchelInventory().fillStackedContents(stackedContents);
    }
}
