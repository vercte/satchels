package net.vercte.satchels.mixin.client.screen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.vercte.satchels.api.ScreenWithSatchel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({CraftingScreen.class, AbstractFurnaceScreen.class, LoomScreen.class})
public abstract class WorkbenchOverridesClickedOutsideScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public WorkbenchOverridesClickedOutsideScreenMixin(T abstractContainerMenu, Inventory inventory, Component component) { super(abstractContainerMenu, inventory, component); }

    @Unique
    private final ScreenWithSatchel satchels$screenWithSatchel = new ScreenWithSatchel();

    @Inject(method = "renderBg", at = @At("HEAD"))
    public void renderSatchelInventory(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        satchels$screenWithSatchel.renderSatchelInventory(guiGraphics, this.leftPos, this.topPos, this.imageHeight);
    }

    @ModifyReturnValue(method = "hasClickedOutside", at = @At("RETURN"))
    public boolean hasClickedOutside(boolean original, double x, double y, int left, int top, int button) {
        return ScreenWithSatchel.hasClickedOutside(x, y, left, top, this.imageHeight) && original;
    }
}
