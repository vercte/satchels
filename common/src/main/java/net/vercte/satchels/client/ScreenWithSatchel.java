package net.vercte.satchels.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.vercte.satchels.ModSprites;
import net.vercte.satchels.satchel.SatchelData;

public class ScreenWithSatchel {
    private float satchelYOffset = -1;

    public void renderSatchelInventory(GuiGraphics graphics, int left, int top, int height) {
        Player player = Minecraft.getInstance().player;
        if(player == null) return;

        SatchelData satchelData = SatchelData.get(player);

        if(this.satchelYOffset == -1) {
            if(satchelData.canAccessSatchelInventory()) this.satchelYOffset = 0;
            else this.satchelYOffset = 27;
        }

        int offsetGoalDisabled = 27;
        if(satchelData.canAccessSatchelInventory()) {
            this.satchelYOffset = Math.max(this.satchelYOffset - (this.satchelYOffset)/5, 0);
        } else {
            this.satchelYOffset = Math.min(this.satchelYOffset + (offsetGoalDisabled-this.satchelYOffset)/5, offsetGoalDisabled);
        }
        if(satchelYOffset > (offsetGoalDisabled - 0.1)) return;

        int satchelXOffset = satchelData.getSatchelOffset() * 18;
        graphics.blitSprite(ModSprites.SATCHEL_INVENTORY, left + 2 + satchelXOffset, top + height - (int)this.satchelYOffset - 1, 118, 27);
    }

//    public void renderSatchelSlot(GuiGraphics graphics, int left, int top) {
//        graphics.blitSprite(ResourceLocation.withDefaultNamespace("container/slot"), left + 151, top + 61, 18, 18);
//    }

    public static boolean hasClickedOutside(boolean original, double x, double y, int left, int top, int height) {
        // Prevent it from ejecting items if we click the satchel portion
        if(original) {
            LocalPlayer player = Minecraft.getInstance().player;
            if(player == null) return true;

            SatchelData satchelData = SatchelData.get(player);
            if(!satchelData.canAccessSatchelInventory()) return true;

            int offset = satchelData.getSatchelOffset();

            int finalLeft = left + (offset * 18);
            boolean clickedLeft = x < finalLeft;
            boolean clickedRight = x >= finalLeft + 120;
            boolean clickedBelow = y >= top + height + 26;
            return clickedLeft || clickedRight || clickedBelow;
        }
        return false;
    }
}
