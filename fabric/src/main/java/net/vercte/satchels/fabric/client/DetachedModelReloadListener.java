package net.vercte.satchels.fabric.client;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.client.model.DetachedModel;

public class DetachedModelReloadListener implements SimpleSynchronousResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return Satchels.at("detached_models");
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        ModelManager manager = Minecraft.getInstance().getModelManager();
        for (DetachedModel detached : DetachedModel.ALL.values()) {
            detached.model = manager.getModel(detached.getModelLocation());
        }
    }
}
