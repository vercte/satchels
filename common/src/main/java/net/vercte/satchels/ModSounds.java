package net.vercte.satchels;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.vercte.satchels.platform.Services;

import java.util.function.Supplier;

public class ModSounds {
    public static Supplier<SoundEvent> SATCHEL_OPEN = Services.REGISTRY.register(BuiltInRegistries.SOUND_EVENT, "satchel_open", () -> SoundEvent.createVariableRangeEvent(Satchels.at("satchel_open")));
    public static Supplier<SoundEvent> SATCHEL_CLOSE = Services.REGISTRY.register(BuiltInRegistries.SOUND_EVENT, "satchel_close", () -> SoundEvent.createVariableRangeEvent(Satchels.at("satchel_close")));

    public static void init() {}
}
