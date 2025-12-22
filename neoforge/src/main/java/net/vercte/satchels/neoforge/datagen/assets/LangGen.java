package net.vercte.satchels.neoforge.datagen.assets;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.vercte.satchels.Satchels;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.system.NonnullDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LangGen extends LanguageProvider {
    private final List<String> keysRegistered = new ArrayList<>();

    public LangGen(PackOutput output) {
        super(output, Satchels.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("key.satchels.toggle_satchel", "Toggle Satchel");
        add("sound.satchels.satchel_rustle", "Satchel rustles");

        add("curios.identifier.satchel", "Satchel");
        add("trinkets.slot.chest.satchel", "Satchel");

        // scan items registry and generate names
        BuiltInRegistries.ITEM.stream()
                .filter(i -> BuiltInRegistries.ITEM.getKey(i).getNamespace().equals(Satchels.ID))
                .filter(i -> keyIsNotRegistered(i.getDescriptionId()))
                .forEach(i -> {
                    String name = generateName(() -> i, Registries.ITEM);
                    addItem(() -> i, name);
                });
    }

    @Override
    @NonnullDefault
    public void add(String key, String value) {
        keysRegistered.add(key);
        super.add(key, value);
    }

    protected boolean keyIsNotRegistered(String key) {
        return !keysRegistered.contains(key);
    }

    @SuppressWarnings("unchecked")
    protected <R, T extends R> String generateName(Supplier<T> object, ResourceKey<? extends Registry<R>> registryKey) {
        Registry<R> registry = (Registry<R>) BuiltInRegistries.REGISTRY.get(registryKey.location());
        assert registry != null;

        ResourceLocation location = registry.getKey(object.get());
        assert location != null;

        // I took this from Registrate. Thank you Registrate
        String[] nameElements = location.getPath()
                .toLowerCase(Locale.ROOT)
                .split("_");
        return Arrays.stream(nameElements)
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }
}
