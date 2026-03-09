package de.pasuki.colorful_redstone.fabric.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.pasuki.colorful_redstone.ColorfulRedstone;
import de.pasuki.colorful_redstone.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ColorfulRedstoneLootTableProvider implements DataProvider {
    private final PackOutput.PathProvider lootTables;

    public ColorfulRedstoneLootTableProvider(FabricDataOutput output) {
        this.lootTables = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_table/blocks");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (DyeColor color : DyeColor.values()) {
            String dustId = ModBlocks.stoneDustId(color);
            String blockId = ModBlocks.stoneBlockId(color);
            String torchId = ModBlocks.stoneTorchId(color);
            String wallTorchId = ModBlocks.stoneWallTorchId(color);
            String repeaterId = ModBlocks.stoneRepeaterId(color);
            String comparatorId = ModBlocks.stoneComparatorId(color);

            futures.add(writeSingleDropLootTable(writer, dustId, dustId));
            futures.add(writeSingleDropLootTable(writer, blockId, blockId));
            futures.add(writeSingleDropLootTable(writer, torchId, torchId));
            futures.add(writeSingleDropLootTable(writer, wallTorchId, torchId));
            futures.add(writeSingleDropLootTable(writer, repeaterId, repeaterId));
            futures.add(writeSingleDropLootTable(writer, comparatorId, comparatorId));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "Colorful Redstone Loot Tables";
    }

    private CompletableFuture<?> writeSingleDropLootTable(CachedOutput writer, String blockId, String dropItemId) {
        ResourceLocation blockLoc = ColorfulRedstone.id(blockId);
        ResourceLocation dropLoc = ColorfulRedstone.id(dropItemId);

        JsonObject root = new JsonObject();
        root.addProperty("type", "minecraft:block");

        JsonArray pools = new JsonArray();
        JsonObject pool = new JsonObject();
        pool.addProperty("rolls", 1.0);
        pool.addProperty("bonus_rolls", 0.0);

        JsonArray entries = new JsonArray();
        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.addProperty("name", dropLoc.toString());
        entries.add(entry);
        pool.add("entries", entries);

        JsonArray conditions = new JsonArray();
        JsonObject survivesExplosion = new JsonObject();
        survivesExplosion.addProperty("condition", "minecraft:survives_explosion");
        conditions.add(survivesExplosion);
        pool.add("conditions", conditions);

        pools.add(pool);
        root.add("pools", pools);

        return DataProvider.saveStable(writer, root, lootTables.json(blockLoc));
    }
}
