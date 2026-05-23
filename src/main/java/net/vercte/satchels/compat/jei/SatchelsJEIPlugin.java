package net.vercte.satchels.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.vercte.satchels.Satchels;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class SatchelsJEIPlugin implements IModPlugin {
    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return Satchels.at("jei");
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(InventoryScreen.class, new SatchelSlotGuiHandler<>());
    }
}
