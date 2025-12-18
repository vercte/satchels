package net.vercte.satchels.satchel;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.vercte.satchels.ModSprites;

public class SatchelHotbarOverlay {
    private static float yOffset = 0;

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode == null || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        int x = graphics.guiWidth() / 2 - 91;
        int y = graphics.guiHeight() - 22;
        RenderSystem.enableDepthTest();

        Player player = mc.player;
        if(player == null) return;

        SatchelData satchelData = SatchelData.get(player);

        int offsetGoal = 22;
        if(satchelData.isSatchelEnabled()) {
            yOffset = Math.max(yOffset - (yOffset)/5, 0);
        } else {
            yOffset = Math.min(yOffset + (offsetGoal-yOffset)/5, offsetGoal);
        }
        if(yOffset > (offsetGoal - 0.1)) return;

        graphics.pose().pushPose();
        graphics.pose().translate(0, yOffset, 750.00);

        int xOffset = satchelData.getSatchelOffset() * 20;
        graphics.blitSprite(ModSprites.SATCHEL_HOTBAR, x + 1 + xOffset, y, 120, 22);

        int selected = player.getInventory().selected;
        boolean selectedInSatchel = satchelData.isSlotInSatchel(selected);
        ResourceLocation selectionSprite = selectedInSatchel ? ModSprites.SATCHEL_HOTBAR_SELECTION : ModSprites.VANILLA_HOTBAR_SELECTION;

        float selectionYOffset = selectedInSatchel ? 0 : -yOffset;

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
