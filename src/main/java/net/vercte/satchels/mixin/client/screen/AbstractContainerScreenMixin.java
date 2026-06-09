package net.vercte.satchels.mixin.client.screen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.vercte.satchels.SatchelsCommonConfig;
import net.vercte.satchels.api.ScreenWithSatchel;
import net.vercte.satchels.content.satchel.SatchelData;
import net.vercte.satchels.content.satchel.SatchelEquipmentSlot;
import net.vercte.satchels.content.satchel.SatchelInventorySlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu> extends Screen {
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

    protected AbstractContainerScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Shadow
    public abstract T getMenu();

    @ModifyExpressionValue(method = {"mouseClicked", "mouseReleased"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;hasClickedOutside(DDIII)Z"))
    public boolean satchels$hasClickedOutside(boolean original, double x, double y, int click) {
        if(!original) return false;

        ResourceLocation location = satchels$getMenuLocation();

        if(location == null) return true;
        if(!SatchelsCommonConfig.isAllowed(location)) return true;
        Tuple<Integer, Integer> offset = SatchelsCommonConfig.getOffset(location);
        return ScreenWithSatchel.hasClickedOutside(x, y, leftPos + offset.getA(), topPos + offset.getB(), this.imageHeight);
    }

    @Inject(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V"))
    public void satchels$renderSatchelInventory(GuiGraphics guiGraphics, int p_283661_, int p_281248_, float p_281886_, CallbackInfo ci) {
        ResourceLocation location = satchels$getMenuLocation();

        if(location == null) return;
        if(!SatchelsCommonConfig.isAllowed(location)) return;

        Tuple<Integer, Integer> offset = SatchelsCommonConfig.getOverlayOffset(location);
        satchels$screenWithSatchel.renderSatchelInventory(guiGraphics, this.leftPos + offset.getA(), this.topPos + offset.getB(), this.imageHeight);
    }

    @WrapOperation(method = "findSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;isActive()Z"))
    public boolean satchels$changeIsActive(Slot slot, Operation<Boolean> original) {
        if(slot instanceof SatchelEquipmentSlot satchelSlot) return satchelSlot.isShown(Minecraft.getInstance().player, this.getMenu());
        return original.call(slot);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;isActive()Z"))
    public boolean satchels$changeIsActiveInRender(Slot slot, Operation<Boolean> original) {
        if(slot instanceof SatchelEquipmentSlot satchelSlot) return satchelSlot.isShown(Minecraft.getInstance().player, this.getMenu());
        return original.call(slot);
    }

    @WrapOperation(method = {"checkHotbarKeyPressed", "checkHotbarMouseClicked"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ClickType;)V", ordinal = 1))
    public void satchels$swapWithSatchelSlotMaybeMouse(AbstractContainerScreen<?> instance, Slot slot, int index, int i, ClickType type, Operation<Void> original) {
        Player player = instance.getMinecraft().player;
        SatchelData data = SatchelData.get(player);
        if(data.canAccess() && data.isSlotInSatchel(i) && hasShiftDown()) {
            int satchelIndex = i - data.getHotbarOffset();

            int satchelSlot = -1;
            for(int j = 0; j < instance.getMenu().slots.size(); j++) {
                Slot possible = instance.getMenu().slots.get(j);
                if(possible instanceof SatchelInventorySlot && possible.getContainerSlot() == satchelIndex) satchelSlot = j;
            }

            if(satchelSlot == -1) {
                original.call(instance, slot, index, i, type);
                return;
            }

            original.call(instance, slot, index, satchelSlot, type);
            return;
        }
        original.call(instance, slot, index, i, type);
    }

    @Unique
    private ResourceLocation satchels$getMenuLocation() {
        return switch (menu) {
            case InventoryMenu ignored -> ResourceLocation.withDefaultNamespace("inventory");
            case CreativeModeInventoryScreen.ItemPickerMenu ignored -> ResourceLocation.withDefaultNamespace("creative_menu");
            case HorseInventoryMenu ignored -> ResourceLocation.withDefaultNamespace("horse");
            case null, default -> {
                try {
                    assert menu != null;
                    yield BuiltInRegistries.MENU.getKey(menu.getType());
                } catch (Exception ignored) {
                    yield null;
                }
            }
        };
    }
}
