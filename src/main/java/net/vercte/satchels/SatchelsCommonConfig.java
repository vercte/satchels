package net.vercte.satchels;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SatchelsCommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue LOG_OPENED_MENU = BUILDER
            .comment("Whether the game should log something when a menu is opened.")
            .define("log_opened_menu", false);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> ALLOWED_MENUS = BUILDER
            .comment("""
                     The menus that the Satchel can have its contents injected into.
                     Format: resource:location [xOffset YOffset] [overlayXOffset overlayYOffset]
                     Example: minecraft:beacon 28 53 28 0
                              minecraft:generic_9x6 0 55
                              minecraft:crafting\
                    """)
            .gameRestart()
            .defineListAllowEmpty("allowed_menus", SatchelsCommonConfig::getMenuDefaults, () -> "minecraft:example", SatchelsCommonConfig::validateMenu);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static final List<ResourceLocation> allowed = new ArrayList<>();
    private static final Map<ResourceLocation, Tuple<Integer, Integer>> offsets = new HashMap<>();
    private static final Map<ResourceLocation, Tuple<Integer, Integer>> overlayOffsets = new HashMap<>();

    public static boolean shouldLog() {
        return LOG_OPENED_MENU.get();
    }

    public static boolean isAllowed(ResourceLocation menuLocation) {
        return allowed.contains(menuLocation);
    }

    public static Tuple<Integer, Integer> getOffset(ResourceLocation menuLocation) {
        return offsets.getOrDefault(menuLocation, new Tuple<>(0, 0));
    }

    public static Tuple<Integer, Integer> getOverlayOffset(ResourceLocation menuLocation) {
        return overlayOffsets.getOrDefault(menuLocation, new Tuple<>(0, 0));
    }

    private static List<String> getMenuDefaults() {
        return List.of(
                "minecraft:inventory",
                "minecraft:crafting",
                "minecraft:crafter_3x3",
                "minecraft:generic_9x1 0 -35 0 -1",
                "minecraft:generic_9x2 0 -17 0 -1",
                "minecraft:generic_9x3 0 1 0 -1",
                "minecraft:generic_9x4 0 19 0 -1",
                "minecraft:generic_9x5 0 37 0 -1",
                "minecraft:generic_9x6 0 55 0 -1",
                "minecraft:shulker_box 0 0 0 -1",
                "minecraft:furnace",
                "minecraft:smoker",
                "minecraft:blast_furnace",
                "minecraft:cartography_table",
                "minecraft:smithing",
                "minecraft:loom",
                "minecraft:stonecutter",
                "minecraft:enchantment",
                "minecraft:anvil",
                "minecraft:grindstone",
                "minecraft:brewing_stand",
                "minecraft:hopper 0 -33",
                "minecraft:merchant 100 0 100 0",
                "minecraft:beacon 28 53 28 0",
                "farmersdelight:cooking_pot",
                "curios:curios_container",
                "accessories:original_menu",
                "create:schematic_table 30 23 30 -8",
                "create:schematicannon 29 77 29 -8",
                "create:toolbox 0 81 0 -8",
                "create:package_port 30 24 30 0"
        );
    }

    private static boolean validateMenu(Object input) {
        if(!(input instanceof String entry)) return false;

        String[] split = entry.split(" ");
        if(split.length == 1) {
            ResourceLocation location = ResourceLocation.tryParse(split[0]);
            return location != null;
        } else if(split.length == 3) {
            ResourceLocation location = ResourceLocation.tryParse(split[0]);
            if(location == null) return false;

            try {
                Integer.parseInt(split[1]);
                Integer.parseInt(split[2]);
                return true;
            } catch (NumberFormatException ignored) {}
        } else if(split.length == 5) {
            ResourceLocation location = ResourceLocation.tryParse(split[0]);
            if(location == null) return false;

            try {
                Integer.parseInt(split[1]);
                Integer.parseInt(split[2]);
                Integer.parseInt(split[3]);
                Integer.parseInt(split[4]);
                return true;
            } catch (NumberFormatException ignored) {}
        }

        return false;
    }

    private static void onConfigUpdate(final ModConfigEvent.Loading event) {
        if(event.getConfig().getType() != ModConfig.Type.COMMON) return;

        allowed.clear();
        offsets.clear();
        overlayOffsets.clear();

        for(String entry : ALLOWED_MENUS.get()) {
            String[] split = entry.split(" ");
            if(split.length == 1) {
                ResourceLocation location = ResourceLocation.parse(split[0]);
                allowed.add(location);
            } else if(split.length == 3) {
                ResourceLocation location = ResourceLocation.parse(split[0]);
                allowed.add(location);

                int x = Integer.parseInt(split[1]);
                int y = Integer.parseInt(split[2]);
                offsets.put(location, new Tuple<>(x, y));
            } else if(split.length == 5) {
                ResourceLocation location = ResourceLocation.parse(split[0]);
                allowed.add(location);

                int x = Integer.parseInt(split[1]);
                int y = Integer.parseInt(split[2]);
                int xOverlay = Integer.parseInt(split[3]);
                int yOverlay = Integer.parseInt(split[4]);
                offsets.put(location, new Tuple<>(x, y));
                overlayOffsets.put(location, new Tuple<>(xOverlay, yOverlay));
            }
        }
    }

    public static void load(IEventBus bus) {
        bus.addListener(SatchelsCommonConfig::onConfigUpdate);
    }
}
