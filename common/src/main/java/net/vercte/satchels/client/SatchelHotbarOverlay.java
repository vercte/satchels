package net.vercte.satchels.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.vercte.satchels.ModSprites;
import net.vercte.satchels.satchel.SatchelData;

public class SatchelHotbarOverlay {
    public static final String ID = "satchel_hotbar";
    public static final SatchelHotbarOverlay INSTANCE = new SatchelHotbarOverlay();

    private long startTime = 0;
    private long endTime = 0;
    private boolean lastState = false;
    private int yOffset = 0;
    private int yOffsetOnChange = 0;

    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode == null || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        int x = graphics.guiWidth() / 2 - 91;
        int y = graphics.guiHeight() - 22;
        RenderSystem.enableDepthTest();

        Player player = mc.player;
        if(player == null) return;

        SatchelData satchelData = SatchelData.get(player);

        boolean enabled = satchelData.isSatchelEnabled();
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
        this.yOffset = (int)LerpFunctions.EXPONENTIAL.lerp(progress, this.yOffsetOnChange, enabled ? 0 : offsetGoal);
        if(this.yOffset == offsetGoal) return;

        graphics.pose().pushPose();
        graphics.pose().translate(0, this.yOffset, 750.00);

        int xOffset = satchelData.getSatchelOffset() * 20;
        graphics.blitSprite(ModSprites.SATCHEL_HOTBAR, x + 1 + xOffset, y, 120, 22);

        int selected = player.getInventory().selected;
        boolean selectedInSatchel = satchelData.isSlotInSatchel(selected);
        ResourceLocation selectionSprite = selectedInSatchel ? ModSprites.SATCHEL_HOTBAR_SELECTION : ModSprites.VANILLA_HOTBAR_SELECTION;

        float selectionYOffset = selectedInSatchel ? 0 : -this.yOffset;

        graphics.pose().pushPose();
        graphics.pose().translate(0, selectionYOffset, 0);

        graphics.blitSprite(selectionSprite, x - 1 + (selected * 20), y - 1, 24, selectedInSatchel ? 24 : 23);

        graphics.pose().popPose();

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
