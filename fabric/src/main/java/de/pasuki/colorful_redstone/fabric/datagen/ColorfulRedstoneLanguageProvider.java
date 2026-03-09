package de.pasuki.colorful_redstone.fabric.datagen;

import com.google.gson.JsonObject;
import de.pasuki.colorful_redstone.datagen.ModLanguageEntries;
import de.pasuki.colorful_redstone.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.concurrent.CompletableFuture;

public class ColorfulRedstoneLanguageProvider implements DataProvider {
    private final PackOutput.PathProvider languageFiles;

    public ColorfulRedstoneLanguageProvider(FabricDataOutput output) {
        this.languageFiles = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "lang");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        JsonObject lang = new JsonObject();

        for (DyeColor color : DyeColor.values()) {
            String dustId = ModBlocks.stoneDustId(color);
            String dustName = ModLanguageEntries.englishDustName(color);
            lang.addProperty("block.colorful_redstone." + dustId, dustName);
            lang.addProperty("item.colorful_redstone." + dustId, dustName);

            String blockId = ModBlocks.stoneBlockId(color);
            String blockName = ModLanguageEntries.englishBlockName(color);
            lang.addProperty("block.colorful_redstone." + blockId, blockName);
            lang.addProperty("item.colorful_redstone." + blockId, blockName);

            String torchId = ModBlocks.stoneTorchId(color);
            String torchName = ModLanguageEntries.englishTorchName(color);
            lang.addProperty("block.colorful_redstone." + torchId, torchName);
            lang.addProperty("item.colorful_redstone." + torchId, torchName);

            String repeaterId = ModBlocks.stoneRepeaterId(color);
            String repeaterName = ModLanguageEntries.englishRepeaterName(color);
            lang.addProperty("block.colorful_redstone." + repeaterId, repeaterName);
            lang.addProperty("item.colorful_redstone." + repeaterId, repeaterName);

            String comparatorId = ModBlocks.stoneComparatorId(color);
            String comparatorName = ModLanguageEntries.englishComparatorName(color);
            lang.addProperty("block.colorful_redstone." + comparatorId, comparatorName);
            lang.addProperty("item.colorful_redstone." + comparatorId, comparatorName);
        }

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("colorful_redstone", "en_us");
        return DataProvider.saveStable(writer, lang, languageFiles.json(id));
    }

    @Override
    public String getName() {
        return "Colorful Redstone EN Lang";
    }
}
