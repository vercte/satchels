package net.vercte.satchels.neoforge.datagen.assets;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.vercte.satchels.ModItems;
import net.vercte.satchels.Satchels;

public class ItemModelGen extends ItemModelProvider {
    public ItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Satchels.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.SATCHEL.get());
    }
}