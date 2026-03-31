package net.vercte.satchels.util.assets;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.vercte.satchels.ModTags;
import net.vercte.satchels.Satchels;
import net.vercte.satchels.ModItems;

public class LangGen extends LanguageProvider {
    public LangGen(PackOutput output) {
        super(output, Satchels.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(ModItems.SATCHEL, "Satchel");
        addItem(ModItems.CRAFTING_MAT, "Crafting Mat");

        addTag(() -> ModTags.SATCHEL, "Satchels");
        add("key.satchels.toggle_satchel", "Toggle Satchel");
        add("sound.satchels.satchel_rustle", "Satchel Rustles");

        add("satchels.options.offset", "Satchel Position");
        add("satchels.options.offset.tooltip", "The position of the Satchel on your hotbar.");
        add("satchels.options.offset.selection", "Slots %s-%s");
    }
}
