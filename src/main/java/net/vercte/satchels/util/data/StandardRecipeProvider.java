package net.vercte.satchels.util.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.vercte.satchels.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class StandardRecipeProvider extends RecipeProvider {
    public StandardRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> recipes) {
        super(output, recipes);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SATCHEL.get())
                .pattern(" s ")
                .pattern("lgl")
                .pattern("sls")
                .define('s', Tags.Items.STRINGS)
                .define('l', Tags.Items.LEATHERS)
                .define('g', Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.CRAFTING_MAT.get())
                .requires(Tags.Items.STRINGS)
                .requires(Items.PAPER)
                .requires(Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
                .requires(Tags.Items.LEATHERS)
                .unlockedBy("has_paper", has(Items.PAPER))
                .unlockedBy("has_leather", has(Tags.Items.LEATHERS))
                .save(output);
    }
}
