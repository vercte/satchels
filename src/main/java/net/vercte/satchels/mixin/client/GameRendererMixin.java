package net.vercte.satchels.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.vercte.satchels.content.craftingmat.CraftingMat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @WrapOperation(method = "lambda$pick$57", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isPickable()Z"))
    private static boolean dontPickMatsWhenCrouching(Entity entity, Operation<Boolean> original) {
        return original.call(entity) &&
                !(
                        entity instanceof CraftingMat &&
                        (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCrouching())
                );
    }
}
