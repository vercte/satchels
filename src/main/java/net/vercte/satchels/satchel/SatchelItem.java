package net.vercte.satchels.satchel;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vercte.satchels.ModSounds;
import net.vercte.satchels.api.SatchelAccess;
import org.jetbrains.annotations.NotNull;

public class SatchelItem extends Item {
    public static final int DEFAULT_COLOR = 0xffaf5d2e; // af5d2e

    public SatchelItem(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        boolean equipped = SatchelAccess.equipSatchelTo(player, hand);
        if(equipped) {
            float pitch = 0.9f + (player.getRandom().nextFloat() / 5);
            player.level().playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    ModSounds.SATCHEL_EQUIP.get(), SoundSource.PLAYERS,
                    1, pitch
            );
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
