package net.vercte.satchels.mixin.compat.figura;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.util.StringRepresentable;
import org.figuramc.figura.model.ParentType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(ParentType.class)
public class ParentTypeMixin {
    @Shadow
    @Final
    @Mutable
    private static ParentType[] $VALUES;

    static {
        vercte$satchels$addVariant("vercte$satchels$satchel", "satchel", false);
    }

    @Invoker("<init>")
    public static ParentType vercte$satchels$invokeInit(String internalName, int internalId, String name, ParentType type) {
        throw new AssertionError();
    }

    @Unique
    private static ParentType vercte$satchels$addVariant(String internalName, String name, boolean user) {
        ArrayList<ParentType> variants = new ArrayList<>(Arrays.asList($VALUES));
        ParentType action = vercte$satchels$invokeInit(internalName, variants.get(variants.size() - 1).ordinal() + 1, name, user);
        variants.add(action);
        $VALUES = variants.toArray(new ClickEvent.Action[0]);
        UNSAFE_CODEC = StringRepresentable.fromEnum(ClickEvent.Action::values).fieldOf("action");
        CODEC = UNSAFE_CODEC.validate(ClickEvent.Action::filterForSerialization);
        return action;
    }
}
