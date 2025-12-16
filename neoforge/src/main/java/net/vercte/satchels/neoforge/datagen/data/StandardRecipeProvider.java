package net.vercte.satchels.neoforge.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
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
    }
}
