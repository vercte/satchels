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
                (Object)this instanceof CartographyTableScreen ||
                (Object)this instanceof StonecutterScreen ||
                (Object)this instanceof GrindstoneScreen ||
                (Object)this instanceof BrewingStandScreen ||
                (Object)this instanceof EnchantmentScreen ||
                (Object)this instanceof BeaconScreen ||
                (Object)this instanceof MerchantScreen ||
                (Object)this instanceof ShulkerBoxScreen ||
                (Object)this instanceof DispenserScreen ||
                (Object)this instanceof CrafterScreen ||
                (Object)this instanceof HopperScreen) {
            return ScreenWithSatchel.hasClickedOutside(original, x, y, left, top, this.imageHeight); // TODO: individualize this for the beacon and merchant
        }
        return original;
    }
}
