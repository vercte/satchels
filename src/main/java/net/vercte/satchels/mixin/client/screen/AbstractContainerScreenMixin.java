package net.vercte.satchels.mixin.client.screen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.vercte.satchels.SatchelsCommonConfig;
import net.vercte.satchels.api.ScreenWithSatchel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin<T extends AbstractContainerMenu> {
    @Unique
    private final ScreenWithSatchel satchels$screenWithSatchel = new ScreenWithSatchel();

    @Shadow
    protected int imageHeight;

    @Shadow
    @Final
    protected T menu;

    @Shadow
    protected int leftPos;

    @Shadow
    protected int topPos;

    @ModifyExpressionValue(method = {"mouseClicked", "mouseReleased"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;hasClickedOutside(DDIII)Z"))
    public boolean hasClickedOutside(boolean original, double x, double y, int click) {
        if(!original) return false;

        boolean inventory = menu instanceof InventoryMenu;
        boolean creative = menu instanceof CreativeModeInventoryScreen.ItemPickerMenu;
        ResourceLocation location = inventory ? ResourceLocation.withDefaultNamespace("inventory") :
                creative ? ResourceLocation.withDefaultNamespace("creative_menu") :
                        BuiltInRegistries.MENU.getKey(menu.getType());

        if(!SatchelsCommonConfig.isAllowed(location)) return true;
        Tuple<Integer, Integer> offset = SatchelsCommonConfig.getOffset(location);
        return ScreenWithSatchel.hasClickedOutside(x, y, leftPos + offset.getA(), topPos + offset.getB(), this.imageHeight);
    }

    @Inject(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V"))
    public void renderSatchelInventory(GuiGraphics guiGraphics, int p_283661_, int p_281248_, float p_281886_, CallbackInfo ci) {
        boolean inventory = menu instanceof InventoryMenu;
        boolean creative = menu instanceof CreativeModeInventoryScreen.ItemPickerMenu;
        ResourceLocation location = inventory ? ResourceLocation.withDefaultNamespace("inventory") :
                creative ? ResourceLocation.withDefaultNamespace("creative_menu") :
                        BuiltInRegistries.MENU.getKey(menu.getType());

        if(!SatchelsCommonConfig.isAllowed(location)) return;

        Tuple<Integer, Integer> offset = SatchelsCommonConfig.getOverlayOffset(location);
        satchels$screenWithSatchel.renderSatchelInventory(guiGraphics, this.leftPos + offset.getA(), this.topPos + offset.getB(), this.imageHeight);
    }
}
