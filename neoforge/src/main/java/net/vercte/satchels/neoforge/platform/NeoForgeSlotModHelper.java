package net.vercte.satchels.neoforge.platform;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.platform.services.ISlotModHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class NeoForgeSlotModHelper implements ISlotModHelper {
    @Override
    public boolean playerHasSatchel(Player player) {
        Optional<ICuriosItemHandler> optCuriosInventory = CuriosApi.getCuriosInventory(player);

        if(optCuriosInventory.isEmpty()) return false;
        return optCuriosInventory.get().isEquipped(this::isItemSatchel);
    }

    @Override
    public boolean playerRendersSatchel(Player player) {
        Optional<ICuriosItemHandler> optCuriosInventory = CuriosApi.getCuriosInventory(player);

        if(optCuriosInventory.isEmpty()) return false;
        AtomicBoolean renders = new AtomicBoolean(false);
        optCuriosInventory.get().getCurios().forEach((s, c) -> {
            IDynamicStackHandler stackHandler = c.getStacks();

            for(int i = 0; i < stackHandler.getSlots(); i++) {
                ItemStack stack = stackHandler.getStackInSlot(i);

                if(stack.isEmpty()) return;
                if(!isItemSatchel(stack)) return;
                NonNullList<Boolean> renderStates = c.getRenders();
                boolean renderable = renderStates.size() > i && renderStates.get(i);
                if(renderable) renders.set(true);
            }
        });

        return playerHasSatchel(player) && renders.get();
    }
}
