package net.vercte.satchels;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.vercte.satchels.platform.Services;
import net.vercte.satchels.satchel.SatchelItem;

import java.util.function.Supplier;

public class ModItems {
    public static final Supplier<Item> SATCHEL = register("satchel", SatchelItem::new);

    private static <T extends Item> Supplier<T> register(String path, Supplier<T> item) {
        return Services.REGISTRY.register(BuiltInRegistries.ITEM, path, item);
    }

    public static void init() {}
}
