package net.vercte.satchels.compat.curios;

import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import net.vercte.satchels.ModSounds;
import net.vercte.satchels.ModTags;
import net.vercte.satchels.api.SatchelAccess;
import net.vercte.satchels.compat.CompatEntrypoint;
import net.vercte.satchels.satchel.SatchelData;
import net.vercte.satchels.satchel.SatchelItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.event.CurioCanUnequipEvent;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class CuriosCompat implements CompatEntrypoint {
    public Map<Player, Integer> satchelTints = new WeakHashMap<>();

    @Override
    public void initialize() {
        SatchelAccess.SATCHEL_EQUIP_CALLBACKS.add(this::equipSatchel);
        SatchelAccess.CAN_ACCESS_PREDICATES.add(this::playerCanAccessSatchel);
        SatchelAccess.IS_VISIBLE_PREDICATES.add(this::playerSatchelIsVisible);
        SatchelAccess.SATCHEL_STACK_GETTERS.add(this::getSatchelStack);
        SatchelAccess.SATCHEL_TINT_GETTERS.add(this::getSatchelTint);

        NeoForge.EVENT_BUS.addListener(this::canUnequipSatchel);
        NeoForge.EVENT_BUS.addListener(this::curioChangeMaybeSatchel);
    }

    public boolean equipSatchel(Player player, InteractionHand hand) {
        Optional<ICuriosItemHandler> optCuriosInventory = CuriosApi.getCuriosInventory(player);
        if(optCuriosInventory.isEmpty()) return false;

        ICuriosItemHandler curiosInventory = optCuriosInventory.get();

        ItemStack stack = player.getItemInHand(hand);

        Optional<SlotResult> optSlotResult = curiosInventory.findFirstCurio(p -> p.is(ModTags.SATCHEL));
        if(optSlotResult.isPresent()) {
            SlotResult result = optSlotResult.get();
            SlotContext context = result.slotContext();

            player.setItemInHand(hand, result.stack());
            curiosInventory.setEquippedCurio(context.identifier(), context.index(), stack);
            return true;
        }

        for(ICurioStacksHandler handler: optCuriosInventory.get().getCurios().values()) {
            IDynamicStackHandler stacks = handler.getStacks();
            for(int i = 0; i < stacks.getSlots(); i++) {
                ItemStack current = stacks.getStackInSlot(i);
                if(!current.isEmpty()) continue;

                stacks.setStackInSlot(i, stack);
                player.setItemInHand(hand, ItemStack.EMPTY);
                return true;
            }
        }

        return false;
    }

    public int getSatchelTint(Player player) {
        return satchelTints.getOrDefault(player, -1);
    }

    public ItemStack getSatchelStack(Player player) {
        Optional<ICuriosItemHandler> optCuriosInventory = CuriosApi.getCuriosInventory(player);
        if(optCuriosInventory.isEmpty()) return ItemStack.EMPTY;

        for(ICurioStacksHandler handler: optCuriosInventory.get().getCurios().values()) {
            IDynamicStackHandler stacks = handler.getStacks();
            for(int i = 0; i < stacks.getSlots(); i++) {
                ItemStack current = stacks.getStackInSlot(i);
                if(current.isEmpty()) continue;
                if(!current.is(ModTags.SATCHEL)) continue;
                return current;
            }
        }

        return ItemStack.EMPTY;
    }

    public boolean playerCanAccessSatchel(Player player) {
        Optional<ICuriosItemHandler> optCuriosInventory = CuriosApi.getCuriosInventory(player);

        if(optCuriosInventory.isEmpty()) return false;
        return optCuriosInventory.get().isEquipped(s -> s.is(ModTags.SATCHEL));
    }

    public boolean playerSatchelIsVisible(Player player) {
        Optional<ICuriosItemHandler> optCuriosInventory = CuriosApi.getCuriosInventory(player);

        if(optCuriosInventory.isEmpty()) return false;

        for(ICurioStacksHandler handler: optCuriosInventory.get().getCurios().values()) {
            IDynamicStackHandler stacks = handler.getStacks();
            for(int i = 0; i < stacks.getSlots(); i++) {
                ItemStack current = stacks.getStackInSlot(i);
                if(current.isEmpty()) continue;
                if(!current.is(ModTags.SATCHEL)) continue;

                NonNullList<Boolean> renderStates = handler.getRenders();
                boolean renderable = renderStates.size() > i && renderStates.get(i);
                if(renderable) return true;
            }
        }

        return false;
    }

    public void canUnequipSatchel(final CurioCanUnequipEvent event) {
        if(!event.getStack().is(ModTags.SATCHEL)) return;

        if(!(event.getSlotContext().entity() instanceof Player player)) return;

        SatchelData satchelData = SatchelData.get(player);
        boolean isEmpty = satchelData.getSatchelInventory().isEmpty();
        event.setUnequipResult(isEmpty ? TriState.DEFAULT : TriState.FALSE);
    }

    public void curioChangeMaybeSatchel(final CurioChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        boolean satchelEquipped = !event.getFrom().is(ModTags.SATCHEL) && event.getTo().is(ModTags.SATCHEL);

        if(event.getTo().is(ModTags.SATCHEL)) satchelTints.put(player, DyedItemColor.getOrDefault(event.getTo(), SatchelItem.DEFAULT_COLOR));

        if (satchelEquipped) {
            float pitch = 0.9f + (player.getRandom().nextFloat() / 5);
            player.level().playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    ModSounds.SATCHEL_EQUIP.get(), SoundSource.PLAYERS,
                    1, pitch
            );
            return;
        }

        if (event.getTo().is(ModTags.SATCHEL)) return;

        satchelTints.remove(player);

        SatchelData satchelData = SatchelData.get(player);
        satchelData.getSatchelInventory().dropAll(false);
        satchelData.setActive(false, true);
        satchelData.sendData();
    }
}
