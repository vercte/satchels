package net.vercte.satchels.compat.figura;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import org.figuramc.figura.entries.FiguraVanillaPart;
import org.figuramc.figura.entries.annotations.FiguraVanillaPartPlugin;
import org.figuramc.figura.model.ParentType;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@FiguraVanillaPartPlugin
public class SatchelsFiguraVanillaPartPlugin implements FiguraVanillaPart {
    private static final String ID = "satchelsfigura";

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public Collection<Pair<String, Function<EntityModel<?>, ModelPart>>> getParts() {
        return List.of();
    }

    @Override
    public Collection<Pair<String, Pair<Function<EntityModel<?>, ModelPart>, ParentType>>> getPartsWithParent() {
        return List.of();
    }
}
