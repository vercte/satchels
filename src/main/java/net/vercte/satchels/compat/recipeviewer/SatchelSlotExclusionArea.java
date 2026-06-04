package net.vercte.satchels.compat.recipeviewer;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.ModTags;
import net.vercte.satchels.content.satchel.SatchelEquipmentSlot;
import net.vercte.satchels.content.satchel.SatchelData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SatchelSlotExclusionArea {
    @NotNull
    public static <T extends AbstractContainerScreen<InventoryMenu>> List<Rect2i> getGuiExtraAreas(T containerScreen) {
        SatchelEquipmentSlot slot = (SatchelEquipmentSlot)containerScreen.getMenu().slots
                .stream()
                .filter(s -> s instanceof SatchelEquipmentSlot)
                .findFirst()
                .orElse(null);
        if(slot == null) return List.of();

        ItemStack carried = containerScreen.getMenu().getCarried();
        SatchelData data = SatchelData.get(containerScreen.getMinecraft().player);

        boolean shown = carried.is(ModTags.SATCHEL) || (
                    data.getSatchelInventory().isEmpty() &&
                    slot.getItem().is(ModTags.SATCHEL)
                );

        if (shown) {
            return List.of(
                    new Rect2i(
                            containerScreen.getGuiLeft() + slot.x - 4,
                            containerScreen.getGuiTop() + slot.y - 4,
                            24,
                            24
                    )
            );
        }
        return List.of();
    }
}
