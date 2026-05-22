package net.vercte.satchels.compat;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;
import net.vercte.satchels.compat.curios.CuriosCompat;
import net.vercte.satchels.compat.vanilla.VanillaCompat;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.function.Supplier;

public enum SatchelsCompat {
    VANILLA("minecraft", VanillaCompat::new, VanillaCompat::shouldBeLoaded), // for vanilla slots
    CURIOS("curios", CuriosCompat::new);

    final String id;
    final boolean isLoaded;

    @Nullable
    final CompatEntrypoint entrypoint;
    final Predicate<ModList> shouldLoad;

    SatchelsCompat(String id, Supplier<CompatEntrypoint> entrypoint, Predicate<ModList> shouldLoad) {
        this.id = id;

        this.entrypoint = entrypoint.get();
        this.isLoaded = LoadingModList.get().getModFileById(id) != null;
        this.shouldLoad = shouldLoad;
    }

    SatchelsCompat(String id, Supplier<CompatEntrypoint> entrypoint) {
        this(id, entrypoint, m -> true);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public static void initialize() {
        for(SatchelsCompat compat : values()) {
            if(!compat.shouldLoad.test(ModList.get())) continue;
            if(compat.entrypoint != null && compat.isLoaded) compat.entrypoint.initialize();
        }
    }
}
