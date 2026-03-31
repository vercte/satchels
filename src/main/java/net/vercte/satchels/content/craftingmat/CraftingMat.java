package net.vercte.satchels.content.craftingmat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vercte.satchels.ModEntities;
import net.vercte.satchels.ModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftingMat extends BlockAttachedEntity {
    private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

    protected int checkInterval;
    protected boolean fixed;

    public CraftingMat(EntityType<? extends BlockAttachedEntity> type, Level level) {
        super(type, level);
    }

    public CraftingMat(EntityType<? extends BlockAttachedEntity> type, Level level, BlockPos pos) {
        super(type, level, pos);
    }

    public CraftingMat(Level level, BlockPos pos) {
        this(ModEntities.CRAFTING_MAT.get(), level, pos);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            this.checkBelowWorld();
            if (this.checkInterval++ == 500) {
                this.checkInterval = 0;
                if (!this.isRemoved() && !this.survives()) {
                    this.discard();
                    this.dropItem(null);
                }
            }
        }
    }

    @Override
    @NotNull
    public InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        if (level().isClientSide) return InteractionResult.SUCCESS;

        player.openMenu(getMenuProvider(level(), pos));
        player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean survives() {
        Level level = this.level();
        if (this.fixed) {
            return true;
        } else if (level.noCollision(this)) {
            return false;
        } else {
            BlockState state = this.level().getBlockState(this.pos);
            return Block.isFaceFull(state.getShape(level, this.pos), Direction.UP);
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float damage) {
        if (this.fixed) {
            return (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || source.isCreativePlayer()) && super.hurt(source, damage);
        } else if (this.isInvulnerableTo(source)) {
            return false;
        }
        return super.hurt(source, damage);
    }

    @Override
    public void dropItem(@Nullable Entity entity) {
        this.playSound(this.getBreakSound(), 1.0F, 1.0F);
        this.gameEvent(GameEvent.BLOCK_CHANGE, entity);

        if(this.fixed) return;
        if(!this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) return;
        if(entity instanceof Player player && player.hasInfiniteMaterials()) return;

        this.spawnAtLocation(this.getItemStack(), 0.6f);
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity);
    }

    @Override
    public void recreateFromPacket(@NotNull ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {}

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        fixed = tag.getBoolean("Fixed");
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putBoolean("Fixed", fixed);
    }

    @Override
    protected void recalculateBoundingBox() {
        BlockState state = level().getBlockState(pos);
        VoxelShape shape = state.getShape(level(), pos);

        AABB box = shape.isEmpty() ? Shapes.block().bounds() : shape.bounds();
        AABB inflated = box.move(pos).inflate(0.025);

        setPosRaw(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        setBoundingBox(inflated);
    }

    @Override
    public ItemStack getPickResult() {
        return getItemStack();
    }

    @Override
    protected boolean repositionEntityAfterLoad() {
        return true;
    }

    @Override
    public void move(@NotNull MoverType mover, @NotNull Vec3 pos) {
        if (!fixed) {
            super.move(mover, pos);
        }
    }

    @Override
    public void push(double x, double y, double z) {
        if (!fixed) push(x, y, z);
    }

    public SoundEvent getBreakSound() {
        return SoundEvents.WOOL_BREAK;
    }

    protected MenuProvider getMenuProvider(Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (id, inventory, player) -> new CraftingMatMenu(id, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE
        );
    }

    protected ItemStack getItemStack() {
        return ModItems.CRAFTING_MAT.toStack();
    }
}
