package net.vercte.satchels.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.vercte.satchels.client.model.ModDetachedModels;
import net.vercte.satchels.satchel.SatchelData;

public class SatchelLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public SatchelLayer(RenderLayerParent<T, M> renderLayerParent, BlockRenderDispatcher blockRenderDispatcher) {
        super(renderLayerParent);
        this.blockRenderDispatcher = blockRenderDispatcher;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, T entity, float yaw, float pitch, float partialTicks, float j, float k, float l) {
        if(!(entity instanceof Player player)) return;

        SatchelData satchelData = SatchelData.get(player);
        if(!satchelData.isSatchelRendered()) return;

        BakedModel satchelModel = ModDetachedModels.SATCHEL_LAYER.get();


        M entityModel = getParentModel();
        if (!(entityModel instanceof HumanoidModel<?> model))
            return;

        poseStack.pushPose();

        model.body.translateAndRotate(poseStack);
        poseStack.translate(-8/16f, 12/16f, 8/16f);
        poseStack.scale(1, -1, -1);

        BlockState state = Blocks.AIR.defaultBlockState();
        BlockPos pos = player.blockPosition();
        this.blockRenderDispatcher.getModelRenderer().tesselateWithoutAO(
                player.level(),
                satchelModel,
                state,
                pos,
                poseStack,
                buffer.getBuffer(Sheets.cutoutBlockSheet()),
                false,
                player.getRandom(),
                0,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();
    }
}
