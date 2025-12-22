package net.vercte.satchels.fabric.platform;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.player.Player;
import net.vercte.satchels.ModTags;
import net.vercte.satchels.platform.services.ISlotModHelper;

import java.util.Optional;

public class FabricSlotModHelper implements ISlotModHelper {
    @Override
    public boolean playerHasSatchel(Player player) {
        Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(player);
        if(optional.isEmpty()) return false;

        TrinketComponent component = optional.get();
        return component.isEquipped(
                s -> s.is(ModTags.SATCHEL)
        );
    }
}
