package net.vercte.satchels.api;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A set of hooks and callbacks to interact with the Satchel.
 */
public class SatchelAccess {
    /**
     * A set of functions to call when the SatchelItem is right-clicked. Meant for equipping the satchel.
     * True means that the satchel was equipped.
     */
    public static Set<SatchelEquipCallback> SATCHEL_EQUIP_CALLBACKS = new HashSet<>();

    /**
     * <p>
     *     A set of predicates to determine if the Satchel can be accessed.
     *     The satchel can be accessed if <i>any</i> of these are true.
     * </p>
     */
    public static Set<Predicate<Player>> CAN_ACCESS_PREDICATES = new HashSet<>();

    /**
     * A set of predicates to determine if the Satchel is visible.
     * The satchel is visible if <i>all</i> of these are true <i>and</i> if the satchel is accessible (see {@link SatchelAccess#CAN_ACCESS_PREDICATES}).
     * <p>
     *     <b>This should be cached...</b> pretty please with a cherry on top?
     * </p>
     */
    public static Set<Predicate<Player>> IS_VISIBLE_PREDICATES = new HashSet<>();

    /**
     * A set of functions to get the Satchel ItemStack.
     * This is used in SatchelLayer to render the satchel.
     * The first function that returns a non-empty item stack is the one rendered.
     */
    public static Set<Function<Player, ItemStack>> SATCHEL_STACK_GETTERS = new HashSet<>();

    /**
     * A set of functions to get the tint of the Satchel. This is used in ScreenWithSatchel
     * to tint the GUI to match the satchel's color. The first function that returns a number != -1
     * is the tint chosen.
     */
    public static Set<Function<Player, Integer>> SATCHEL_TINT_GETTERS = new HashSet<>();

    /**
     * Checks if a player can access their satchel.
     * @param player The <code>Player</code> that this check concerns.
     * @return Whether the <code>Player</code> can access their satchel.
     */
    public static boolean equipSatchelTo(Player player, InteractionHand hand) {
        return SATCHEL_EQUIP_CALLBACKS.stream().anyMatch(p -> p.equip(player, hand));
    }

    /**
     * Checks if a player can access their satchel.
     * @param player The <code>Player</code> that this check concerns.
     * @return Whether the <code>Player</code> can access their satchel.
     */
    public static boolean canAccessSatchel(Player player) {
        return (
                CAN_ACCESS_PREDICATES.isEmpty() ||
                CAN_ACCESS_PREDICATES.stream().anyMatch(p -> p.test(player))
        );
    }

    /**
     * Checks if the satchel model is visible on a player.
     * @param player The <code>Player</code> that this check concerns.
     * @return Whether the satchel model is visible on the player.
     */
    public static boolean satchelIsVisible(Player player) {
        return canAccessSatchel(player) && (
                IS_VISIBLE_PREDICATES.isEmpty() ||
                IS_VISIBLE_PREDICATES.stream().allMatch(p -> p.test(player))
        );
    }

    /**
     * Gets the satchel to be rendered on the player.
     * @param player The <code>Player</code> that this query concerns.
     * @return The ItemStack that represents the rendered satchel.
     */
    public static ItemStack getSatchelStack(Player player) {
        for(Function<Player, ItemStack> getter : SATCHEL_STACK_GETTERS) {
            ItemStack stack = getter.apply(player);
            if(!stack.isEmpty()) return stack;
        }
        return ItemStack.EMPTY;
    }

    /**
     * Gets the satchel tint to be used in UI.
     * @param player The <code>Player</code> that this query concerns.
     * @return The first valid satchel tint. Returns -1 if no valid color is found.
     */
    public static int getSatchelTint(Player player) {
        for(Function<Player, Integer> getter : SATCHEL_TINT_GETTERS) {
            int tint = getter.apply(player);
            if(tint != -1) return tint;
        }
        return -1;
    }

    @FunctionalInterface
    public interface SatchelEquipCallback {
        boolean equip(Player player, InteractionHand hand);
    }
}
