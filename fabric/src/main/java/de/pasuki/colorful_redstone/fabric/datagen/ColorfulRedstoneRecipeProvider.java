package de.pasuki.colorful_redstone.fabric.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.pasuki.colorful_redstone.ColorfulRedstone;
import de.pasuki.colorful_redstone.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ColorfulRedstoneRecipeProvider implements DataProvider {
    private final PackOutput.PathProvider recipes;

    public ColorfulRedstoneRecipeProvider(FabricDataOutput output) {
        this.recipes = output.createPathProvider(PackOutput.Target.DATA_PACK, "recipe");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (DyeColor color : DyeColor.values()) {
            String dustId = ModBlocks.stoneDustId(color);
            String blockId = ModBlocks.stoneBlockId(color);
            String torchId = ModBlocks.stoneTorchId(color);
            String repeaterId = ModBlocks.stoneRepeaterId(color);
            String comparatorId = ModBlocks.stoneComparatorId(color);

            futures.add(writeDustFromDyeRecipe(writer, color, dustId));
            futures.add(writeBlockFromDustRecipe(writer, dustId, blockId));
            futures.add(writeDustFromBlockRecipe(writer, dustId, blockId));
            futures.add(writeTorchFromDustRecipe(writer, dustId, torchId));
            futures.add(writeColoredRepeaterRecipe(writer, color, repeaterId));
            futures.add(writeColoredComparatorRecipe(writer, color, comparatorId));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "Colorful Redstone Recipes";
    }

    private CompletableFuture<?> writeDustFromDyeRecipe(CachedOutput writer, DyeColor color, String dustId) {
        String recipeName = dustId + "_from_redstone_and_" + color.getName() + "_dye";

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shapeless");
        recipe.addProperty("category", "redstone");

        JsonArray ingredients = new JsonArray();
        JsonObject redstone = new JsonObject();
        redstone.addProperty("item", "minecraft:redstone");
        ingredients.add(redstone);

        JsonObject dye = new JsonObject();
        dye.addProperty("item", toDyeItemId(color));
        ingredients.add(dye);
        recipe.add("ingredients", ingredients);

        JsonObject result = new JsonObject();
        result.addProperty("id", ColorfulRedstone.MOD_ID + ":" + dustId);
        result.addProperty("count", 1);
        recipe.add("result", result);

        return DataProvider.saveStable(writer, recipe, recipes.json(ColorfulRedstone.id(recipeName)));
    }

    private CompletableFuture<?> writeBlockFromDustRecipe(CachedOutput writer, String dustId, String blockId) {
        String recipeName = blockId + "_from_" + dustId;

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "redstone");

        JsonArray pattern = new JsonArray();
        pattern.add("###");
        pattern.add("###");
        pattern.add("###");
        recipe.add("pattern", pattern);

        JsonObject key = new JsonObject();
        JsonObject hash = new JsonObject();
        hash.addProperty("item", ColorfulRedstone.MOD_ID + ":" + dustId);
        key.add("#", hash);
        recipe.add("key", key);

        JsonObject result = new JsonObject();
        result.addProperty("id", ColorfulRedstone.MOD_ID + ":" + blockId);
        result.addProperty("count", 1);
        recipe.add("result", result);

        return DataProvider.saveStable(writer, recipe, recipes.json(ColorfulRedstone.id(recipeName)));
    }

    private CompletableFuture<?> writeDustFromBlockRecipe(CachedOutput writer, String dustId, String blockId) {
        String recipeName = dustId + "_from_" + blockId;

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shapeless");
        recipe.addProperty("category", "redstone");

        JsonArray ingredients = new JsonArray();
        JsonObject block = new JsonObject();
        block.addProperty("item", ColorfulRedstone.MOD_ID + ":" + blockId);
        ingredients.add(block);
        recipe.add("ingredients", ingredients);

        JsonObject result = new JsonObject();
        result.addProperty("id", ColorfulRedstone.MOD_ID + ":" + dustId);
        result.addProperty("count", 9);
        recipe.add("result", result);

        return DataProvider.saveStable(writer, recipe, recipes.json(ColorfulRedstone.id(recipeName)));
    }

    private CompletableFuture<?> writeTorchFromDustRecipe(CachedOutput writer, String dustId, String torchId) {
        String recipeName = torchId + "_from_" + dustId;

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "redstone");

        JsonArray pattern = new JsonArray();
        pattern.add("X");
        pattern.add("#");
        recipe.add("pattern", pattern);

        JsonObject key = new JsonObject();
        JsonObject x = new JsonObject();
        x.addProperty("item", ColorfulRedstone.MOD_ID + ":" + dustId);
        key.add("X", x);

        JsonObject hash = new JsonObject();
        hash.addProperty("item", "minecraft:stick");
        key.add("#", hash);
        recipe.add("key", key);

        JsonObject result = new JsonObject();
        result.addProperty("id", ColorfulRedstone.MOD_ID + ":" + torchId);
        result.addProperty("count", 1);
        recipe.add("result", result);

        return DataProvider.saveStable(writer, recipe, recipes.json(ColorfulRedstone.id(recipeName)));
    }

    private CompletableFuture<?> writeColoredRepeaterRecipe(CachedOutput writer, DyeColor color, String repeaterId) {
        String recipeName = repeaterId + "_from_repeater_and_" + color.getName() + "_dye";

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shapeless");
        recipe.addProperty("category", "redstone");

        JsonArray ingredients = new JsonArray();
        JsonObject repeater = new JsonObject();
        repeater.addProperty("item", "minecraft:repeater");
        ingredients.add(repeater);

        JsonObject dye = new JsonObject();
        dye.addProperty("item", toDyeItemId(color));
        ingredients.add(dye);
        recipe.add("ingredients", ingredients);

        JsonObject result = new JsonObject();
        result.addProperty("id", ColorfulRedstone.MOD_ID + ":" + repeaterId);
        result.addProperty("count", 1);
        recipe.add("result", result);

        return DataProvider.saveStable(writer, recipe, recipes.json(ColorfulRedstone.id(recipeName)));
    }

    private CompletableFuture<?> writeColoredComparatorRecipe(CachedOutput writer, DyeColor color, String comparatorId) {
        String recipeName = comparatorId + "_from_comparator_and_" + color.getName() + "_dye";

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shapeless");
        recipe.addProperty("category", "redstone");

        JsonArray ingredients = new JsonArray();
        JsonObject comparator = new JsonObject();
        comparator.addProperty("item", "minecraft:comparator");
        ingredients.add(comparator);

        JsonObject dye = new JsonObject();
        dye.addProperty("item", toDyeItemId(color));
        ingredients.add(dye);
        recipe.add("ingredients", ingredients);

        JsonObject result = new JsonObject();
        result.addProperty("id", ColorfulRedstone.MOD_ID + ":" + comparatorId);
        result.addProperty("count", 1);
        recipe.add("result", result);

        return DataProvider.saveStable(writer, recipe, recipes.json(ColorfulRedstone.id(recipeName)));
    }

    private static String toDyeItemId(DyeColor color) {
        return "minecraft:" + color.getName() + "_dye";
    }
}
