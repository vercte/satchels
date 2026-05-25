package net.vercte.satchels;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.vercte.satchels.api.MenuWithSatchel;
import net.vercte.satchels.content.satchel.SatchelEquipmentSlot;
import net.vercte.satchels.content.satchel.SatchelData;

public class SatchelsEventHooks {
    public static void playerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        for(Slot slot : event.getEntity().inventoryMenu.slots) {
            if(slot instanceof SatchelEquipmentSlot equipmentSlot) equipmentSlot.onLoad(event.getEntity());
        }
        if(!(player instanceof ServerPlayer sp)) return;

        SatchelData.get(sp).sendData();
    }

    public static void playerClone(final PlayerEvent.Clone event) {
        if(!event.isWasDeath()) return;

        if(!event.getOriginal().level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) return;

        if(event.getOriginal().hasData(ModAttachmentTypes.SATCHEL_SLOT)) {
            ItemStack previous = event.getOriginal().getData(ModAttachmentTypes.SATCHEL_SLOT).getStackInSlot(0);

            ItemStack newStack = previous.copy();
            event.getEntity().getData(ModAttachmentTypes.SATCHEL_SLOT).setStackInSlot(0, newStack);
            event.getEntity().syncData(ModAttachmentTypes.SATCHEL_SLOT);
            // FIXME: the slot doesn't re-sync for some fuckin reason
        }

        SatchelData original = SatchelData.get(event.getOriginal());
        SatchelData.get(event.getEntity()).copyFrom(original);
    }

    public static void creativeTabBuild(final BuildCreativeModeTabContentsEvent event) {
        if(!event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) return;
        event.insertAfter(new ItemStack(Items.LEAD), ModItems.SATCHEL.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    public static void onContainerOpen(final PlayerContainerEvent.Open event) {
        SatchelsEventHooks.onMenuOpen(event.getEntity(), event.getContainer());
    }

    public static void onMenuOpen(Player player, AbstractContainerMenu menu) {
        SatchelData satchelData = SatchelData.get(player);

        boolean inventory = menu instanceof InventoryMenu;
        boolean creative = menu instanceof CreativeModeInventoryScreen.ItemPickerMenu;
        if(inventory || creative) return;

        ResourceLocation menuLocation = BuiltInRegistries.MENU.getKey(menu.getType());

        assert menuLocation != null;
        if(SatchelsCommonConfig.shouldLog()) LogUtils.getLogger().info("satchels: opened {}", menuLocation);
        if(!SatchelsCommonConfig.isAllowed(menuLocation)) return;

        Tuple<Integer, Integer> offset = SatchelsCommonConfig.getOffset(menuLocation);
        MenuWithSatchel.addInventorySlots(satchelData, menu::addSlot, 8 + offset.getA(), 170 + offset.getB(), 18);
    }
}
