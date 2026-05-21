package net.vercte.satchels;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class SatchelsCommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<List<? extends String>> ALLOWED_MENUS = BUILDER
            .comment(" The menus that the Satchel can have its contents injected into.")
            .defineListAllowEmpty("allowed_menus", SatchelsCommonConfig::getMenuDefaults, () -> "minecraft:example", SatchelsCommonConfig::isValidResourceLocation);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean isValidResourceLocation(Object location) {
        if(!(location instanceof String string)) return false;
        try {
            ResourceLocation.parse(string);
            return true;
        } catch (Exception e) {
            LogUtils.getLogger().error("Invalid resource location in config/satchels-common.toml: '{}'", location);
        }
        return false;
    }

    public static boolean isAllowed(ResourceLocation menuLocation) {
        return ALLOWED_MENUS.get().contains(menuLocation.toString());
    }

    private static List<String> getMenuDefaults() {
        return List.of(
                "minecraft:inventory",
                "minecraft:crafting",
                "minecraft:crafter_3x3",
                "minecraft:generic_9x1",
                "minecraft:generic_9x2",
                "minecraft:generic_9x3",
                "minecraft:generic_9x4",
                "minecraft:generic_9x5",
                "minecraft:generic_9x6",
                "minecraft:shulker_box",
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
                "farmersdelight:cooking_pot"
        );
    }
}
