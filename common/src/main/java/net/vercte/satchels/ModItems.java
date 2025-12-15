package net.vercte.satchels;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.vercte.satchels.platform.Services;

import java.util.function.Supplier;

public class ModItems {
    public static Supplier<Item> SATCHEL = register("satchel", () -> new Item(new Item.Properties().stacksTo(1)));

    private static <T extends Item> Supplier<T> register(String path, Supplier<T> item) {
        return Services.REGISTRY.register(BuiltInRegistries.ITEM, path, item);
    }

    public static void init() {}
}
