package net.vercte.satchels;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vercte.satchels.content.craftingmat.CraftingMat;

public class ModEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Satchels.ID);

    public static final DeferredHolder<EntityType<?>, EntityType<CraftingMat>> CRAFTING_MAT = ENTITIES.register(
            "crafting_mat",
            () -> EntityType.Builder.<CraftingMat>of(CraftingMat::new, MobCategory.MISC)
                    .sized(1.05f, 1.05f)
                    .eyeHeight(0)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("satchels:crafting_mat")
    );

    public static void loadAndListen(IEventBus bus) { ENTITIES.register(bus); }
}
