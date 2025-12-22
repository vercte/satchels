package net.vercte.satchels;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static final TagKey<Item> SATCHEL = TagKey.create(Registries.ITEM, Satchels.at("satchel"));
}
