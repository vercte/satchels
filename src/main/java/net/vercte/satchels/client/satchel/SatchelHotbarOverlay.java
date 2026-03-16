package net.vercte.satchels.client.satchel;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.vercte.satchels.api.SatchelAccess;
import net.vercte.satchels.client.ModSprites;
import net.vercte.satchels.client.animation.LerpFunctions;
import net.vercte.satchels.client.animation.LerpHelper;
import net.vercte.satchels.satchel.SatchelData;
import net.vercte.satchels.satchel.SatchelItem;

import java.awt.*;

public class SatchelHotbarOverlay {
    public static final String ID = "satchel_hotbar";
    public static final SatchelHotbarOverlay INSTANCE = new SatchelHotbarOverlay();

    private long startTime = 0;
    private long endTime = 0;
    private boolean lastState = false;
    private int yOffset = 0;
    private int yOffsetOnChange = 0;

    private int lastColor = SatchelItem.DEFAULT_COLOR;

    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode == null || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        int x = graphics.guiWidth() / 2 - 91;
        int y = graphics.guiHeight() - 22;
        RenderSystem.enableDepthTest();

        Player player = mc.player;
        if(player == null) return;

        int satchelTint = SatchelAccess.getSatchelTint(player);
        if(satchelTint != -1) lastColor = satchelTint;

        SatchelData satchelData = SatchelData.get(player);

        boolean enabled = satchelData.isActive();
        boolean stateChanged = this.lastState != enabled;

        int offsetGoal = 24;
        long currentTime = Util.getMillis();

        if(stateChanged) {
            startTime = currentTime;
            endTime = currentTime + 300;
            yOffsetOnChange = this.yOffset;
            lastState = enabled;
        }

        float progress = LerpHelper.getProgress(currentTime, this.startTime, this.endTime);
        this.yOffset = (int) LerpFunctions.EXPONENTIAL.lerp(progress, this.yOffsetOnChange, enabled ? 0 : offsetGoal);
        if(this.yOffset == offsetGoal) return;

        int red = FastColor.ARGB32.red(lastColor);
        int green = FastColor.ARGB32.green(lastColor);
        int blue = FastColor.ARGB32.blue(lastColor);
        int alpha = FastColor.ARGB32.alpha(lastColor);
        graphics.setColor(
                red / 255f,
                green / 255f,
                blue / 255f,
                alpha / 255f
        );

        graphics.pose().pushPose();
        graphics.pose().translate(0, this.yOffset, 750.00);

        int xOffset = satchelData.getHotbarOffset() * 20;
        graphics.blitSprite(ModSprites.SATCHEL_HOTBAR, x + 1 + xOffset, y, 120, 22);

        int selected = player.getInventory().selected;
        boolean selectedInSatchel = satchelData.isSlotInSatchel(selected);
        ResourceLocation selectionSprite = selectedInSatchel ? ModSprites.SATCHEL_HOTBAR_SELECTION : ModSprites.VANILLA_HOTBAR_SELECTION;

        float selectionYOffset = selectedInSatchel ? 0 : -this.yOffset;

        graphics.pose().pushPose();
        graphics.pose().translate(0, selectionYOffset, 0);

        if(selectedInSatchel) {
            float[] hsb = Color.RGBtoHSB(red, green, blue, null);
            hsb[0] = Math.max(hsb[0] - 0.01f, 0f);
            hsb[1] = Math.max(hsb[1] - 0.1f, 0f);
            hsb[2] = Math.min(hsb[2] + 0.1f, 1f);

            int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
            graphics.setColor(
                    FastColor.ARGB32.red(rgb) / 255f,
                    FastColor.ARGB32.green(rgb) / 255f,
                    FastColor.ARGB32.blue(rgb) / 255f,
                    alpha / 255f
            );
        } else {
            graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        graphics.blitSprite(selectionSprite, x - 1 + (selected * 20), y - 1, 24, selectedInSatchel ? 24 : 23);

        graphics.pose().popPose();

        if(selectedInSatchel) graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        for(int i = 0; i < satchelData.getSatchelInventory().getContainerSize(); i++) {
            ItemStack stack = satchelData.getSatchelInventory().getItem(i);
            SatchelHotbarOverlay.renderSlot(graphics, x + (i*20) + 3 + xOffset, y + 3, deltaTracker, player, stack, i+1);
        }
        graphics.pose().popPose();
    }

    // copied from Gui#renderSlot
    private static void renderSlot(GuiGraphics guiGraphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int k) {
        if (!itemStack.isEmpty()) {
            float f = itemStack.getPopTime() - deltaTracker.getGameTimeDeltaPartialTick(false);
            if (f > 0.0F) {
                float g = 1.0F + f / 5.0F;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((float)(x + 8), (float)(y + 12), 0.0F);
                guiGraphics.pose().scale(1.0F / g, (g + 1.0F) / 2.0F, 1.0F);
                guiGraphics.pose().translate((float)(-(x + 8)), (float)(-(y + 12)), 0.0F);
            }

            guiGraphics.renderItem(player, itemStack, x, y, k);
            if (f > 0.0F) {
                guiGraphics.pose().popPose();
            }

            guiGraphics.renderItemDecorations(Minecraft.getInstance().font, itemStack, x, y);
        }
    }
}