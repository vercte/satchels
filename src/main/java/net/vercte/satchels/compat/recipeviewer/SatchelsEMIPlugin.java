package net.vercte.satchels.compat.recipeviewer;

import dev.emi.emi.api.*;
import dev.emi.emi.api.widget.Bounds;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.List;
import java.util.function.Consumer;

@EmiEntrypoint
public class SatchelsEMIPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addExclusionArea(InventoryScreen.class, new SatchelsEMIExclusionArea<>());
    }

    private static class SatchelsEMIExclusionArea<T extends AbstractContainerScreen<InventoryMenu>> implements EmiExclusionArea<T> {
        @Override
        public void addExclusionArea(T screen, Consumer<Bounds> consumer) {
            List<Rect2i> rects = SatchelSlotExclusionArea.getGuiExtraAreas(screen);
            for(Rect2i r: rects) {
                consumer.accept(
                        new Bounds(r.getX(), r.getY(), r.getWidth(), r.getHeight())
                );
            }
        }
    }
}
