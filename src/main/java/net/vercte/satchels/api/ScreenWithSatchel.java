package net.vercte.satchels.api;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.ModTags;
import net.vercte.satchels.client.ModSprites;
import net.vercte.satchels.client.SatchelsClientConfig;
import net.vercte.satchels.client.animation.LerpFunctions;
import net.vercte.satchels.client.animation.LerpHelper;
import net.vercte.satchels.compat.SatchelsCompat;
import net.vercte.satchels.content.satchel.SatchelEquipmentSlot;
import net.vercte.satchels.content.satchel.SatchelData;
import net.vercte.satchels.content.satchel.SatchelItem;

/**
 * <p>A class that contains utilities for implementing the Satchel rendering (background, animation) into screens.</p>
 * <p>This needs to be initialized, as it has animation data stored within.</p>
 */
public class ScreenWithSatchel {
    private float satchelYOffset = -1;
    private float yOffsetOnChange = 0;
    private long inventoryTweenStartTime = 0;
    private long inventoryTweenEndTime = 0;
    private boolean lastInventoryState = false;

    private float slotXOffset = -1;
    private long slotTweenStartTime = 0;
    private long slotTweenEndTime = 0;
    private float xOffsetOnChange = 0;
    private boolean lastSlotState = false;

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

        if(satchelYOffset == -1) {
            satchelYOffset = satchelData.canAccess() ? 0 : 27;
        }

        int offsetGoal = 27;
        boolean enabled = satchelData.canAccess();
        boolean stateChanged = lastInventoryState != enabled;
        long currentTime = Util.getMillis();

        if(stateChanged) {
            inventoryTweenStartTime = currentTime;
            inventoryTweenEndTime = currentTime + 300;
            yOffsetOnChange = satchelYOffset;
            lastInventoryState = enabled;
        }

        float progress = LerpHelper.getProgress(currentTime, inventoryTweenStartTime, inventoryTweenEndTime);
        if(SatchelsClientConfig.shouldAnimateGUI()) satchelYOffset = (int) LerpFunctions.EXPONENTIAL.lerp(progress, yOffsetOnChange, enabled ? 0 : offsetGoal);
        else satchelYOffset = enabled ? 0 : offsetGoal;
        if(satchelYOffset == offsetGoal) return;

        int satchelXOffset = satchelData.getHotbarOffset() * 18;

        int satchelTint = SatchelAccess.getSatchelTint(player);
        if(satchelTint != -1) lastColor = satchelTint;

        graphics.setColor(
                FastColor.ARGB32.red(lastColor) / 255f,
                FastColor.ARGB32.green(lastColor) / 255f,
                FastColor.ARGB32.blue(lastColor) / 255f,
                FastColor.ARGB32.alpha(lastColor) / 255f
        );
        graphics.blitSprite(ModSprites.SATCHEL_INVENTORY, left + 2 + satchelXOffset, top + height - (int)satchelYOffset - 1, 118, 27);
        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void renderSatchelSlot(GuiGraphics graphics, int left, int top, int width, int height) {
        if(!SatchelsCompat.VANILLA.isLoaded()) return;

        Player player = Minecraft.getInstance().player;
        if(player == null) return;

        SatchelData data = SatchelData.get(player);
        ItemStack carried = player.containerMenu.getCarried();
        SatchelEquipmentSlot slot = (SatchelEquipmentSlot)player.containerMenu.slots.stream()
                .filter(s -> s instanceof SatchelEquipmentSlot)
                .findFirst()
                .orElse(null);

        if(slot == null) return;

        boolean shown = carried.is(ModTags.SATCHEL) || (
                data.getSatchelInventory().isEmpty() &&
                        slot.getItem().is(ModTags.SATCHEL)
        );

        if(slotXOffset == -1) {
            slotXOffset = shown ? 0 : 27;
        }

        int offsetGoal = -27;
        boolean stateChanged = lastSlotState != shown;
        long currentTime = Util.getMillis();

        if(stateChanged) {
            slotTweenStartTime = currentTime;
            slotTweenEndTime = currentTime + 300;
            xOffsetOnChange = slotXOffset;
            lastSlotState = shown;
        }

        float progress = LerpHelper.getProgress(currentTime, slotTweenStartTime, slotTweenEndTime);
        if(SatchelsClientConfig.shouldAnimateGUI()) slotXOffset = (int) LerpFunctions.EXPONENTIAL.lerp(progress, xOffsetOnChange, shown ? 0 : offsetGoal);
        else slotXOffset = shown ? 0 : offsetGoal;
        if(slotXOffset == offsetGoal) return;


        int x = left + width + (int)slotXOffset - 1;
        int y = top + height - 30;
        graphics.blitSprite(ModSprites.SATCHEL_SLOT_INVENTORY, x, y, 27, 28);
        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

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
