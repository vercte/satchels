package net.vercte.satchels.mixin.client.screen;

import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import net.vercte.satchels.client.SatchelsClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsScreen.class)
public abstract class ControlsScreenMixin extends OptionsSubScreen {
    public ControlsScreenMixin(Screen p_345104_, Options p_346116_, Component p_344987_) {
        super(p_345104_, p_346116_, p_344987_);
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    public void addSatchelOffsetOption(CallbackInfo ci) {
        assert this.list != null;
        this.list.addSmall(SatchelsClientConfig.getOffsetOption());
    }
}
