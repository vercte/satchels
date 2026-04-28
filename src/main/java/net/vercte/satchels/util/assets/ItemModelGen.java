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

        ItemModelBuilder satchelHeld = nested().parent(item_generated)
                .texture("layer0", satchel)
                .texture("layer1", Satchels.at("item/satchel_clip"));

        ItemModelBuilder satchelWorn = nested().parent(getExistingFile(Satchels.at("item/satchel_worn")));

        withExistingParent(satchel.toString(), "item/generated")
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(satchelHeld)
                .perspective(ItemDisplayContext.HEAD, satchelWorn);

        ResourceLocation crafting_mat = Satchels.at("item/crafting_mat");

        ItemModelBuilder heldModel = nested().parent(item_generated)
                .texture("layer0", crafting_mat.withSuffix("_paper"))
                .texture("layer1", crafting_mat.withSuffix("_grid"))
                .texture("layer2", crafting_mat);

        ItemModelBuilder entityModel = nested().parent(getExistingFile(Satchels.at("block/crafting_mat")));

        withExistingParent(crafting_mat.toString(), "item/generated")
                .texture("layer0", crafting_mat.withSuffix("_paper"))
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(heldModel)
                .perspective(ItemDisplayContext.valueOf("SATCHELS_CRAFTING_MAT"), entityModel);
    }
}
