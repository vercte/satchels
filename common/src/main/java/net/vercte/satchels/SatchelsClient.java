package net.vercte.satchels;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.vercte.satchels.config.ClientConfig;
import net.vercte.satchels.network.ToggleSatchelPacketC2S;
import net.vercte.satchels.satchel.SatchelData;
import org.apache.logging.log4j.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class SatchelsClient {
    public static Lazy<KeyMapping> KEYMAPPING_TOGGLE_SATCHEL = Lazy.lazy(
            () -> new KeyMapping("key.satchels.toggle_satchel", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KeyMapping.CATEGORY_INVENTORY)
    );

    public static void init() {
        ClientConfig.load();
        if(!ClientConfig.getConfigFile().exists()) ClientConfig.saveConfig();

        LogUtils.getLogger().info("offset: {}", ClientConfig.getSatchelOffset());
    }

    public static void endClientTick() {
        while(KEYMAPPING_TOGGLE_SATCHEL.get().consumeClick()) {
            SatchelData satchelData = SatchelData.get(Minecraft.getInstance().player);

            if(!satchelData.canAccessSatchelInventory()) continue;
            boolean willEnable = !satchelData.isSatchelEnabled();
            satchelData.setSatchelEnabled(willEnable, true);
            ToggleSatchelPacketC2S.send(willEnable);
        }
    }
}
