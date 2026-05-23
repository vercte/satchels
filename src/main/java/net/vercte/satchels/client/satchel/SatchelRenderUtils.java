package net.vercte.satchels.client.satchel;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class SatchelRenderUtils {
    public static void renderSlot(GuiGraphics guiGraphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int k) {
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