package net.vercte.satchels.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.SatchelsClient;

@EventBusSubscriber
@Mod(value = Satchels.ID, dist = Dist.CLIENT)
public class SatchelsNeoForgeClient {
    @SubscribeEvent
    public static void endClientTick(final ClientTickEvent.Post event) {
        SatchelsClient.endClientTick();
    }
}
