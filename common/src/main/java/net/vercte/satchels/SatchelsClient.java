package net.vercte.satchels;

import net.minecraft.client.Minecraft;
import net.vercte.satchels.network.ToggleSatchelPacketC2S;
import net.vercte.satchels.satchel.SatchelData;

public class SatchelsClient {
    public static void endClientTick() {
        while(Satchels.KEYMAPPING_TOGGLE_SATCHEL.get().consumeClick()) {
            SatchelData satchelData = SatchelData.get(Minecraft.getInstance().player);

            if(!satchelData.canAccessSatchelInventory()) continue;
            boolean willEnable = !satchelData.isSatchelEnabled();
            satchelData.setSatchelEnabled(willEnable);
            ToggleSatchelPacketC2S.send(willEnable);
        }
    }
}
