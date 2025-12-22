package net.vercte.satchels.neoforge.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.util.List;
import java.util.Set;

public class SatchelsNeoForgeMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(mixinClassName.contains("compat.curios")) {
            // only do this if curios is here (don't want to crash anything!)
            return isClassPresent("top.theillusivec4.curios.CuriosConstants");
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() { return List.of(); }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    // thank you hamarb123, MacOSInputFixes: https://github.com/hamarb123/MCMacOSInputFixes/blob/main/src/client/java/com/hamarb123/macos_input_fixes/client/MixinPlugin.java#L146
    private static boolean isClassPresent(String className) {
        try {
            MixinService.getService().getBytecodeProvider().getClassNode(className);
            return true;
        } catch (ClassNotFoundException ignored) {
            // Class isn't present, skip this mixin.
            return false;
        } catch (Exception e) {
            // Something else went wrong which might be more serious.
            e.printStackTrace();
            return false;
        }
    }
}
