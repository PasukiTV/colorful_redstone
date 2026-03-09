package de.pasuki.colorful_redstone.fabric.datagen;

import de.pasuki.colorful_redstone.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ColorfulRedstoneDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ColorfulRedstoneModelProvider::new);
        pack.addProvider(ColorfulRedstoneLootTableProvider::new);
        pack.addProvider(ColorfulRedstoneRecipeProvider::new);
        pack.addProvider(ColorfulRedstoneLanguageProvider::new);
        pack.addProvider(ColorfulRedstoneGermanLanguageProvider::new);
        pack.addProvider(ColorfulRedstoneBlockTagProvider::new);
        pack.addProvider(ColorfulRedstoneItemTagProvider::new);
    }
}
