package de.pasuki.colorful_redstone.fabric.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.pasuki.colorful_redstone.ColorfulRedstone;
import de.pasuki.colorful_redstone.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class ColorfulRedstoneBlockTagProvider implements DataProvider {
    private final PackOutput.PathProvider blockTags;

    public ColorfulRedstoneBlockTagProvider(FabricDataOutput output) {
        this.blockTags = output.createPathProvider(PackOutput.Target.DATA_PACK, "tags/block");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        JsonObject root = new JsonObject();
        root.addProperty("replace", false);

        JsonArray values = new JsonArray();
        for (var color : net.minecraft.world.item.DyeColor.values()) {
            values.add(ColorfulRedstone.MOD_ID + ":" + ModBlocks.stoneDustId(color));
        }
        root.add("values", values);

        return DataProvider.saveStable(writer, root, blockTags.json(ColorfulRedstone.id("colored_redstone_wires")));
    }

    @Override
    public String getName() {
        return "Colorful Redstone Block Tags";
    }
}
