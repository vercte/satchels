package net.vercte.satchels.content.craftingmat;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CraftingMatMenu extends CraftingMenu {
    final ContainerLevelAccess access;

    public CraftingMatMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(id, inventory, access);
        this.access = access;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.access.evaluate(
                (l, p) -> {
                    AABB box = AABB.ofSize(Vec3.atCenterOf(p), 1, 1, 1);
                    return !l.getEntitiesOfClass(CraftingMat.class, box).isEmpty();
                }, false
        );
    }
}
