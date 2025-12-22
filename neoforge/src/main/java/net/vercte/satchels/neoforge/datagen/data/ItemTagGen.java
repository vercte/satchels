package net.vercte.satchels.neoforge.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.vercte.satchels.ModItems;
import net.vercte.satchels.ModTags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ItemTagGen extends ItemTagsProvider {
    public ItemTagGen(PackOutput arg, CompletableFuture<HolderLookup.Provider> holderLookup) {
        super(arg, holderLookup, CompletableFuture.supplyAsync(() -> null)); // this feels fucked up but I have no blocks
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        TagKey<Item> CURIOS_SATCHEL = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", "satchel"));
        TagKey<Item> TRINKETS_SATCHEL = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("trinkets", "chest/satchel"));

        tag(ModTags.SATCHEL)
                .add(ModItems.SATCHEL.get());

        tag(CURIOS_SATCHEL)
                .addTag(ModTags.SATCHEL);

        tag(TRINKETS_SATCHEL)
                .addTag(ModTags.SATCHEL);
    }
}
