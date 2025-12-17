package net.vercte.satchels.mixin.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CrafterScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.vercte.satchels.satchel.ScreenWithSatchel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrafterScreen.class)
public abstract class CrafterScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public CrafterScreenMixin(T abstractContainerMenu, Inventory inventory, Component component) { super(abstractContainerMenu, inventory, component); }

    @Unique private final ScreenWithSatchel satchels$screenWithSatchel = new ScreenWithSatchel();

    @Inject(method = "renderBg", at = @At("HEAD"))
    public void renderSatchelInventory(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        satchels$screenWithSatchel.renderSatchelInventory(guiGraphics, this.leftPos, this.topPos, this.imageHeight);
    }
}
