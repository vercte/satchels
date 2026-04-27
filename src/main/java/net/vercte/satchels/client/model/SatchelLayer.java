package net.vercte.satchels.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.vercte.satchels.api.SatchelAccess;
import net.vercte.satchels.client.SatchelsClientConfig;
import org.jetbrains.annotations.NotNull;

public class SatchelLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ItemRenderer itemRenderer;

    public SatchelLayer(RenderLayerParent<T, M> renderLayerParent, ItemRenderer itemRenderer) {
        super(renderLayerParent);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, @NotNull T entity, float yaw, float pitch, float partialTicks, float j, float k, float l) {
        if(!(entity instanceof Player player)) return;
        if(!SatchelsClientConfig.shouldRenderSatchel()) return;
        if(!SatchelAccess.satchelIsVisible(player)) return;

        M entityModel = getParentModel();
        if (!(entityModel instanceof HumanoidModel<?> model))
            return;

        poseStack.pushPose();

        model.body.translateAndRotate(poseStack);
        poseStack.translate(0, 4/16f, 0);
        poseStack.scale(-1, -1, 1);

        ItemStack satchelStack = SatchelAccess.getSatchelStack(player);
        itemRenderer.renderStatic(
                player,
                satchelStack,
                ItemDisplayContext.HEAD,
                false,
                poseStack,
                buffer,
                player.level(),
                light,
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();
    }
}