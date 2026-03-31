package net.vercte.satchels.util.assets;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.vercte.satchels.Satchels;

public class ItemModelGen extends ItemModelProvider {
    public ItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Satchels.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile item_generated = new ModelFile.UncheckedModelFile("item/generated");

        ResourceLocation satchel = Satchels.at("item/satchel");

        dualContext(
                satchel,
                nested().parent(item_generated)
                        .texture("layer0", Satchels.at("item/satchel"))
                        .texture("layer1", Satchels.at("item/satchel_clip")),
                nested().parent(getExistingFile(Satchels.at("item/satchel_worn"))),
                ItemDisplayContext.HEAD
        );

        ResourceLocation crafting_mat = Satchels.at("item/crafting_mat");

        dualContext(
                crafting_mat,
                nested().parent(item_generated)
                        .texture("layer0", Satchels.at("item/crafting_mat")),
                nested().parent(getExistingFile(Satchels.at("block/crafting_mat"))),
                ItemDisplayContext.valueOf("SATCHELS_CRAFTING_MAT")
        );
    }

    private void dualContext(ResourceLocation location, ItemModelBuilder base, ItemModelBuilder secondary, ItemDisplayContext context) {
        withExistingParent(location.toString(), "item/generated")
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(base)
                .perspective(context, secondary);
    }
}
