package net.vercte.satchels.compat.recipeviewer;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.vercte.satchels.Satchels;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class SatchelsJEIPlugin implements IModPlugin {
    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return Satchels.at("jei");
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(InventoryScreen.class, new SatchelsJEIExclusionArea<>());
    }

    private static class SatchelsJEIExclusionArea<T extends AbstractContainerScreen<InventoryMenu>> implements IGuiContainerHandler<T> {
        @Override
        @NotNull
        public List<Rect2i> getGuiExtraAreas(@NotNull T containerScreen) {
            return SatchelSlotExclusionArea.getGuiExtraAreas(containerScreen);
        }
    }
}
