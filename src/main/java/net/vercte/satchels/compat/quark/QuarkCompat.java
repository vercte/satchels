package net.vercte.satchels.compat.quark;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.vercte.satchels.compat.CompatEntrypoint;
import net.vercte.satchels.content.satchel.SatchelData;
import org.violetmoon.quark.api.IUsageTickerOverride;
import org.violetmoon.quark.api.event.UsageTickerEvent;

import java.util.function.Predicate;

public class QuarkCompat implements CompatEntrypoint {
    @Override
    public void initialize() {
        NeoForge.EVENT_BUS.addListener(this::addSatchelToUsageTicker);
    }

    private void addSatchelToUsageTicker(final UsageTickerEvent.GetCount event) {
        SatchelData satchelData = SatchelData.get(event.player);
        if(!satchelData.canAccess()) return;

        int playerSelectedInSatchel = satchelData.isSlotInSatchel(event.player.getInventory().selected) ?
                satchelData.convertToSatchelIndex(event.player.getInventory().selected) :
                -1;

        int extraCount = 0;
        Predicate<ItemStack> predicate = s -> ItemStack.isSameItemSameComponents(s, event.currentStack);
        for(int i = 0; i < satchelData.getSatchelInventory().getItems().size(); i++) {
            if(playerSelectedInSatchel == i) continue;

            ItemStack stack = satchelData.getSatchelInventory().getItem(i);
            if(predicate.test(stack))
                extraCount += stack.getCount();
            else if(stack.getItem() instanceof IUsageTickerOverride over) {
                extraCount += over.getUsageTickerCountForItem(stack, predicate);
            }
        }

        event.setResultCount(event.getResultCount() + extraCount);
    }
}
