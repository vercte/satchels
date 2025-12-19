package net.vercte.satchels.neoforge.client;

import com.mojang.logging.LogUtils;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.vercte.satchels.client.model.DetachedModel;

import java.util.Map;

public class NeoForgeDetachedModelPopulator {
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        for (ResourceLocation modelLocation : DetachedModel.ALL.keySet()) {
            event.register(ModelResourceLocation.standalone(modelLocation));
        }
    }

    public static void onBakingCompleted(ModelEvent.BakingCompleted event) {
        Map<ModelResourceLocation, BakedModel> models = event.getModels();

        for (DetachedModel detached : DetachedModel.ALL.values()) {
            detached.model = models.get(ModelResourceLocation.standalone(detached.getModelLocation()));
            LogUtils.getLogger().info("model: {}, at {}", detached, detached.getModelLocation());
        }
    }
}
