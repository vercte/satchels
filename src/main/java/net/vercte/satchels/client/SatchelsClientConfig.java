package net.vercte.satchels.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.network.PacketDistributor;
import net.vercte.satchels.network.packets.SatchelOffsetUpdatePacketC2S;
import net.vercte.satchels.content.satchel.SatchelData;

public class SatchelsClientConfig {
    private static int satchelOffset = 0;
    private static boolean swapWithShiftKey = false;
    private static boolean satchelLayer = false;
    private static boolean guiAnimation = false;

    public static int getSatchelOffset() {
        return satchelOffset;
    }
    public static boolean shouldSwapWithShiftKey() { return swapWithShiftKey; }

    public static boolean shouldRenderSatchel() { return satchelLayer; }
    public static boolean shouldAnimateGUI() { return guiAnimation; }

    // region Spec
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static { BUILDER.push("gameplay"); }

    private static final ModConfigSpec.IntValue SATCHEL_OFFSET = BUILDER
            .comment("The offset in the position of your satchel on your hotbar. 0 = covers slots 1-6, 3 = covers slots 4-9")
            .defineInRange("satchel_offset", 0, 0, 3);

    private static final ModConfigSpec.BooleanValue SHIFT_SWAP = BUILDER
            .comment("Whether items should swap into the Satchel Hotbar when shift is held while pressing a hotbar key.")
            .define("shift_swap", true);

    static { BUILDER.pop(); BUILDER.push("rendering"); }

    private static final ModConfigSpec.BooleanValue SATCHEL_LAYER = BUILDER
            .comment("Whether the satchel should render on players when equipped.")
            .define("satchel_layer", true);

    private static final ModConfigSpec.BooleanValue GUI_ANIMATION = BUILDER
            .comment("Whether the satchel should animate in the GUI.")
            .define("gui_animation", true);

    static final ModConfigSpec SPEC = BUILDER.build();
    // endregion

    // region Controls Hook
    public static void updateOffset(int offset) {
        SATCHEL_OFFSET.set(offset);
        SATCHEL_OFFSET.save();
    }
    // endregion

    private static void onConfigUpdate(final ModConfigEvent event) {
        if(event.getConfig().getType() != ModConfig.Type.CLIENT) return;

        satchelOffset = SATCHEL_OFFSET.get();
        swapWithShiftKey = SHIFT_SWAP.get();

        satchelLayer = SATCHEL_LAYER.get();
        guiAnimation = GUI_ANIMATION.get();

        if(event.getConfig().getType() == ModConfig.Type.CLIENT && Minecraft.getInstance().getConnection() != null) {
            SatchelData.get(Minecraft.getInstance().player).setHotbarOffset(satchelOffset);
            PacketDistributor.sendToServer(new SatchelOffsetUpdatePacketC2S(satchelOffset));
        }
    }

    public static void load(IEventBus modEventBus) {
        modEventBus.addListener(SatchelsClientConfig::onConfigUpdate);
    }
}
