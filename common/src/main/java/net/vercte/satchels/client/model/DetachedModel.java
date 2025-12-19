package net.vercte.satchels.client.model;

import com.google.common.collect.MapMaker;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.vercte.satchels.Satchels;
import org.jetbrains.annotations.UnknownNullability;

import java.util.concurrent.ConcurrentMap;

// Heavily Referenced from Flywheel's PartialModels. By referenced I mean almost completely copied because I want
// the entity layers to support resource packs
// A model that's not loaded as a block or item model, but still gets loaded nonetheless.
public class DetachedModel {
    public static final ConcurrentMap<ResourceLocation, DetachedModel> ALL = new MapMaker().weakValues().makeMap();

    private final ResourceLocation modelLocation;
    public BakedModel model;

    private DetachedModel(ResourceLocation modelLocation) {
        this.modelLocation = modelLocation;
    }

    public static DetachedModel of(ResourceLocation modelLocation) {
        return ALL.computeIfAbsent(modelLocation, DetachedModel::new);
    }

    public static DetachedModel of(String path) {
        return ALL.computeIfAbsent(Satchels.at(path), DetachedModel::new);
    }

    @UnknownNullability
    public BakedModel get() {
        return model;
    }

    public ResourceLocation getModelLocation() {
        return modelLocation;
    }
}
