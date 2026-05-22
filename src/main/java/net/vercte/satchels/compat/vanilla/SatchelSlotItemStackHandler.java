package net.vercte.satchels.compat.vanilla;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.vercte.satchels.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SatchelSlotItemStackHandler extends ItemStackHandler {
    public SatchelSlotItemStackHandler() {
        super();
    }

    public static SatchelSlotItemStackHandler fromStack(ItemStack stack) {
        SatchelSlotItemStackHandler handler = new SatchelSlotItemStackHandler();
        handler.stacks.set(0, stack);
        return handler;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.is(ModTags.SATCHEL);
    }

    public static class SyncHandler implements AttachmentSyncHandler<SatchelSlotItemStackHandler> {
        @Override
        public void write(@NotNull RegistryFriendlyByteBuf buf, @NotNull SatchelSlotItemStackHandler attachment, boolean initialSync) {
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, attachment.getStackInSlot(0));
        }

        @Override
        public @Nullable SatchelSlotItemStackHandler read(@NotNull IAttachmentHolder holder, @NotNull RegistryFriendlyByteBuf buf, @Nullable SatchelSlotItemStackHandler previousValue) {
            return fromStack(ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
        }
    }
}
