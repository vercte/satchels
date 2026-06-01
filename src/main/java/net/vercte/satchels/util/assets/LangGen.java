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

        addTag(() -> ModTags.SATCHEL, "Satchels");
        add("key.satchels.toggle_satchel", "Toggle Satchel");
        add("sound.satchels.satchel_rustle", "Satchel Rustles");

        add("satchels.options.offset", "Satchel Position");
        add("satchels.options.offset.tooltip", "The position of the Satchel on your hotbar.");
        add("satchels.options.offset.selection", "Slots %s-%s");

        add("satchels.configuration.satchel_offset", "Satchel Position");
        add("satchels.configuration.satchel_layer", "Render Satchel on Players");
        add("satchels.configuration.gui_animation", "GUI/HUD Animation");

        add("satchels.configuration.log_opened_menu", "Log Opened Menus");
        add("satchels.configuration.allowed_menus", "Menus with Satchel Slots");
    }
}
