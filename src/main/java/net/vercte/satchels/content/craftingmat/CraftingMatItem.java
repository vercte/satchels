package net.vercte.satchels.content.craftingmat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class CraftingMatItem extends Item {
    public CraftingMatItem(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public InteractionResult useOn(@NotNull UseOnContext context) {
        boolean canPlace = canPlaceOn(context);
        if(!canPlace) return InteractionResult.PASS;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        context.getItemInHand().shrink(1);

        if(!level.isClientSide) {
            CraftingMat mat = new CraftingMat(level, pos);
            level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS);
            level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, pos);
            level.addFreshEntity(mat);
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private boolean canPlaceOn(@NotNull UseOnContext context) {
        if(context.getClickedFace() != Direction.UP) return false;
        if(context.isInside()) return false;

        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        VoxelShape shape = state.getShape(level, pos);

        if(level.isOutsideBuildHeight(pos)) return false;
        if(player != null && !player.mayUseItemAt(pos, context.getClickedFace(), context.getItemInHand())) return false;

        return Block.isFaceFull(shape, Direction.UP); // TODO: detect other mats in the block
    }
}
