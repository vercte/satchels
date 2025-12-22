package net.vercte.satchels.neoforge.datagen.data.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.vercte.satchels.Satchels;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class CuriosProvider extends CuriosDataProvider {
    public CuriosProvider(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(Satchels.ID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
        this.createSlot("satchel")
                .icon(Satchels.at("slot/satchel"))
                .order(65);
        this.createEntities("satchel")
                .addPlayer()
                .addSlots("satchel");
    }
}
