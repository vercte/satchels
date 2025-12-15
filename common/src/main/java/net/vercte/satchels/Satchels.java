package net.vercte.satchels;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.vercte.satchels.platform.Services;
import org.apache.logging.log4j.util.Lazy;
import org.lwjgl.glfw.GLFW;

// TODO: Datagen
public final class Satchels {
    public static final String ID = "satchels";

    public static Lazy<KeyMapping> KEYMAPPING_TOGGLE_SATCHEL = Lazy.lazy(
            () -> new KeyMapping("key.satchels.toggle_satchel", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KeyMapping.CATEGORY_INVENTORY)
    );

    public static void init() {
        LogUtils.getLogger().info("Satchels loaded on platform: {}", Services.PLATFORM.getPlatform());

        ModItems.init();
    }

    public static ResourceLocation at(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
