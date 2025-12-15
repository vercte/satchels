package net.vercte.satchels.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.satchel.SatchelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {
    public InventoryScreenMixin(InventoryMenu abstractContainerMenu, Inventory inventory, Component component) { super(abstractContainerMenu, inventory, component); }

    @Unique
    private static final ResourceLocation satchels$satchelInventorySprite = Satchels.at("satchel_inventory");

    @Inject(method = "renderBg", at = @At("TAIL"))
    public void renderBg(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if(player == null) return;

        guiGraphics.blitSprite(ResourceLocation.withDefaultNamespace("container/slot"), this.leftPos + 151, this.topPos + 61, 18, 18);

        if(SatchelData.get(player).canAccessSatchelInventory()) {
            guiGraphics.blitSprite(satchels$satchelInventorySprite, this.leftPos + 2, this.topPos + 165, 118, 27);
        }
    }

    @ModifyReturnValue(method = "hasClickedOutside", at = @At("RETURN"))
    public boolean hasClickedOutside(boolean original, double x, double y, int left, int top, int button) {
        // Prevent it from ejecting items if we click the satchel portion
        if(original) {
            LocalPlayer player = Minecraft.getInstance().player;
            if(player == null) return true;
            if(!SatchelData.get(player).canAccessSatchelInventory()) return true;

            boolean clickedLeft = x < left;
            boolean clickedRight = x >= left + 120;
            boolean clickedBelow = y >= top + this.imageHeight + 26;
            return clickedLeft || clickedRight || clickedBelow;
        }
        return false;
    }
}
