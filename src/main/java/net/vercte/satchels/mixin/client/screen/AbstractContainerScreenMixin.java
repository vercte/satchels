package net.vercte.satchels.mixin.client.screen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.screens.inventory.*;
import net.vercte.satchels.api.ScreenWithSatchel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    @Shadow
    protected int imageHeight;

    @SuppressWarnings("ConstantValue")
    @ModifyReturnValue(method = "hasClickedOutside", at = @At("RETURN"))
    public boolean hasClickedOutside(boolean original, double x, double y, int left, int top, int button) {
        if((Object)this instanceof ContainerScreen ||
                (Object)this instanceof ItemCombinerScreen<?> ||
                (Object)this instanceof CartographyTableScreen ||
                (Object)this instanceof StonecutterScreen ||
                (Object)this instanceof GrindstoneScreen ||
                (Object)this instanceof BrewingStandScreen ||
                (Object)this instanceof EnchantmentScreen ||
                (Object)this instanceof ShulkerBoxScreen ||
                (Object)this instanceof DispenserScreen ||
                (Object)this instanceof CrafterScreen ||
                (Object)this instanceof HopperScreen) {

            return ScreenWithSatchel.hasClickedOutside(x, y, left, top, this.imageHeight) && original;
        } else if((Object)this instanceof BeaconScreen) {
            return ScreenWithSatchel.hasClickedOutside(x, y, left + 28, top, this.imageHeight) && original;
        } else if((Object)this instanceof MerchantScreen) {
            return ScreenWithSatchel.hasClickedOutside(x, y, left + 100, top, this.imageHeight) && original;
        }
        return original;
    }
}
