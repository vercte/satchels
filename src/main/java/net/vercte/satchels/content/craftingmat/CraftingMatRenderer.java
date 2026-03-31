package net.vercte.satchels.content.craftingmat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;

public class CraftingMatRenderer<T extends CraftingMat> extends EntityRenderer<T> {
    private final EntityRendererProvider.Context context;

    public CraftingMatRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void render(@NotNull T entity, float idk, float partialTicks, @NotNull PoseStack pose, @NotNull MultiBufferSource buffer, int light) {
        Level level = entity.level();
        BlockPos above = entity.getPos().above();
        int lightAbove = level.getBrightness(LightLayer.BLOCK, above);
        int skyLightAbove = level.getBrightness(LightLayer.SKY, above);
        int newLight = LightTexture.pack(lightAbove, skyLightAbove);

        pose.pushPose();

        pose.scale(1.001f, 1.001f, 1.001f);

        ItemStack stack = entity.getItemStack();
        context.getItemRenderer().renderStatic(
                null,
                stack,
                ItemDisplayContext.valueOf("SATCHELS_CRAFTING_MAT"),
                false,
                pose,
                buffer,
                level,
                newLight,
                OverlayTexture.NO_OVERLAY,
                0
        );

        pose.popPose();
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation") // it's fiiiiiine
    public ResourceLocation getTextureLocation(@NotNull T entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
