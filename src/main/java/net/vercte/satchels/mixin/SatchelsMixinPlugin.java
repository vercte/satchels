package net.vercte.satchels.mixin;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.neoforged.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class SatchelsMixinPlugin implements IMixinConfigPlugin {
    private final Object2BooleanMap<String> modLoadedCache = new Object2BooleanOpenHashMap<>();

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Thanks, Sable!
        if (mixinClassName.startsWith("net.vercte.satchels.mixin.compat.")) {
            final String[] parts = mixinClassName.split("\\.");
            if (parts.length < 5) {
                return true;
            }

            final String modId = parts[5];
            return this.modLoadedCache.computeIfAbsent(
                    modId,
                    x -> FMLLoader.getLoadingModList().getModFileById((String)x) != null
            );
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
