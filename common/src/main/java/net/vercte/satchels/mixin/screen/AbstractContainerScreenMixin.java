package net.vercte.satchels.mixin.screen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.screens.inventory.*;
import net.vercte.satchels.satchel.ScreenWithSatchel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    @Shadow
    protected int imageHeight;

    @ModifyReturnValue(method = "hasClickedOutside", at = @At("RETURN"))
    public boolean hasClickedOutside(boolean original, double x, double y, int left, int top, int button) {
        if((Object)this instanceof ContainerScreen ||
                (Object)this instanceof ItemCombinerScreen<?> ||
                (Object)this instanceof CartographyTableScreen) {
            return ScreenWithSatchel.hasClickedOutside(original, x, y, left, top, this.imageHeight);
        }
        return original;
    }
}
