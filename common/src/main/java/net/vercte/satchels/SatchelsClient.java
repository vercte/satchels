package net.vercte.satchels;

import com.mojang.logging.LogUtils;
import net.vercte.satchels.network.ToggleSatchelPacket;

public class SatchelsClient {
    public static void endClientTick() {
        while(Satchels.KEYMAPPING_TOGGLE_SATCHEL.get().consumeClick()) {
            LogUtils.getLogger().info("bap");
            ToggleSatchelPacket.INSTANCE.send();
        }
    }
}
