package net.vercte.satchels;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.vercte.satchels.compat.vanilla.SatchelSlotItemStackHandler;

import java.util.function.Supplier;

public class ModAttachmentTypes {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Satchels.ID);

    public static final Supplier<AttachmentType<SatchelSlotItemStackHandler>> SATCHEL_SLOT = ATTACHMENT_TYPES.register(
            "satchel_slot", () -> AttachmentType.serializable(SatchelSlotItemStackHandler::new)
                    .sync(new SatchelSlotItemStackHandler.SyncHandler())
                    .build()
    );

    public static void loadAndRegister(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
