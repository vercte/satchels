package net.vercte.satchels;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.vercte.satchels.platform.Services;

// TODO: Datagen
public final class Satchels {
    public static final String ID = "satchels";

    public static void init() {
        LogUtils.getLogger().info("Satchels loaded on platform: {}", Services.PLATFORM.getPlatform());

        ModItems.init();
    }

    public static ResourceLocation at(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
