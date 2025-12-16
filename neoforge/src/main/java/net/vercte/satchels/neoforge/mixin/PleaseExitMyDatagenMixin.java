package net.vercte.satchels.neoforge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

// thanks Fuzss: https://github.com/Fuzss/puzzleslib/blob/41f9cec7de7848e4a5e2ce474ce01f88d8c54f6e/1.21.1/NeoForge/src/main/java/fuzs/puzzleslib/neoforge/mixin/DatagenModLoaderNeoForgeMixin.java
@Mixin(value = DatagenModLoader.class, remap = false)
public class PleaseExitMyDatagenMixin {
    @WrapOperation(method = "begin",
            at = @At(value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/data/event/GatherDataEvent$DataGeneratorConfig;runAll()V"))
    private static void begin(GatherDataEvent.DataGeneratorConfig dataGeneratorConfig, Operation<Void> operation) {
        // architectury loom does not exit the data run configuration, this will allow it to do so
        //noinspection ConstantValue
        if (!FMLEnvironment.production && isRunningDataGen()) {
            try {
                operation.call(dataGeneratorConfig);
            } catch (Throwable throwable) {
                LogUtils.getLogger().error("Data generation failed", throwable);
            }
            System.exit(0);
        } else {
            operation.call(dataGeneratorConfig);
        }
    }

    @Shadow
    public static boolean isRunningDataGen() {
        throw new RuntimeException();
    }
}
