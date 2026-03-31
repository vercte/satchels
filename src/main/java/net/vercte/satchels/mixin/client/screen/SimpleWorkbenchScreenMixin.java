package net.vercte.satchels.mixin.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.vercte.satchels.api.ScreenWithSatchel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({
        AnvilScreen.class, BeaconScreen.class, BrewingStandScreen.class,
        CartographyTableScreen.class, ContainerScreen.class, CrafterScreen.class,
        DispenserScreen.class, EnchantmentScreen.class,  GrindstoneScreen.class,
        HopperScreen.class, ItemCombinerScreen.class, MerchantScreen.class,
        ShulkerBoxScreen.class, StonecutterScreen.class
})
public abstract class SimpleWorkbenchScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public SimpleWorkbenchScreenMixin(T abstractContainerMenu, Inventory inventory, Component component) { super(abstractContainerMenu, inventory, component); }

    @Unique private final ScreenWithSatchel satchels$screenWithSatchel = new ScreenWithSatchel();

    @Inject(method = "renderBg", at = @At("HEAD"))
    public void renderSatchelInventory(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        int leftPosOffset = 0;
        if((Object)this instanceof BeaconScreen) leftPosOffset = 28;
        if((Object)this instanceof MerchantScreen) leftPosOffset = 100;

        int topPosOffset = (Object)this instanceof ContainerScreen ? -1 : 0;
        satchels$screenWithSatchel.renderSatchelInventory(guiGraphics, this.leftPos + leftPosOffset, this.topPos + topPosOffset, this.imageHeight);
    }
}