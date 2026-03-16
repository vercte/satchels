package net.vercte.satchels.api;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.vercte.satchels.client.ModSprites;
import net.vercte.satchels.client.animation.LerpFunctions;
import net.vercte.satchels.client.animation.LerpHelper;
import net.vercte.satchels.satchel.SatchelData;
import net.vercte.satchels.satchel.SatchelItem;

import java.awt.*;

/**
 * <p>A class that contains utilities for implementing the Satchel rendering (background, animation) into screens.</p>
 * <p>This needs to be initialized, as it has animation data stored within.</p>
 */
public class ScreenWithSatchel {
    private float satchelYOffset = -1;
    private float yOffsetOnChange = 0;
    private long startTime = 0;
    private long endTime = 0;
    private boolean lastState = false;

    private int lastColor = SatchelItem.DEFAULT_COLOR;

    /**
     * Render the satchel background.
     * @param graphics The <code>GuiGraphics</code> passed to the render screen. Easily obtainable from <code>Screen#renderBg</code>.
     * @param left The left edge of the background (when {@link SatchelData#getHotbarOffset()} is 0).
     * @param top The position of the top edge of your screen.
     * @param height The height of your screen.
     */
    public void renderSatchelInventory(GuiGraphics graphics, int left, int top, int height) {
        Player player = Minecraft.getInstance().player;
        if(player == null) return;

        SatchelData satchelData = SatchelData.get(player);

        if(this.satchelYOffset == -1) {
            if(satchelData.canAccess()) this.satchelYOffset = 0;
            else this.satchelYOffset = 27;
        }

        int offsetGoal = 27;
        boolean enabled = satchelData.canAccess();
        boolean stateChanged = this.lastState != enabled;
        long currentTime = Util.getMillis();

        if(stateChanged) {
            startTime = currentTime;
            endTime = currentTime + 300;
            yOffsetOnChange = this.satchelYOffset;
            lastState = enabled;
        }

        float progress = LerpHelper.getProgress(currentTime, this.startTime, this.endTime);
        this.satchelYOffset = (int) LerpFunctions.EXPONENTIAL.lerp(progress, this.yOffsetOnChange, enabled ? 0 : offsetGoal);
        if(this.satchelYOffset == offsetGoal) return;

        int satchelXOffset = satchelData.getHotbarOffset() * 18;

        int satchelTint = SatchelAccess.getSatchelTint(player);
        if(satchelTint != -1) lastColor = satchelTint;

        graphics.setColor(
                FastColor.ARGB32.red(lastColor) / 255f,
                FastColor.ARGB32.green(lastColor) / 255f,
                FastColor.ARGB32.blue(lastColor) / 255f,
                FastColor.ARGB32.alpha(lastColor) / 255f
        );
        graphics.blitSprite(ModSprites.SATCHEL_INVENTORY, left + 2 + satchelXOffset, top + height - (int)this.satchelYOffset - 1, 118, 27);
        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

//    public void renderSatchelSlot(GuiGraphics graphics, int left, int top) {
//        graphics.blitSprite(ResourceLocation.withDefaultNamespace("container/slot"), left + 151, top + 61, 18, 18);
//    }

    /**
     * Use to determine if a click is within the satchel.
     * @param x The x-position of the mouse.
     * @param y the y-position of the mouse.
     * @param left The left edge of the bounds (when {@link SatchelData#getHotbarOffset()} is 0).
     * @param top The position of the top edge of your screen.
     * @param height The height of your screen.
     */
    public static boolean hasClickedOutside(double x, double y, int left, int top, int height) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return true;

        SatchelData satchelData = SatchelData.get(player);
        if(!satchelData.canAccess()) return true;

        int offset = satchelData.getHotbarOffset();

        int finalLeft = left + (offset * 18);
        boolean clickedLeft = x < finalLeft;
        boolean clickedRight = x >= finalLeft + 120;
        boolean clickedBelow = y >= top + height + 26;
        return clickedLeft || clickedRight || clickedBelow;
    }
}
