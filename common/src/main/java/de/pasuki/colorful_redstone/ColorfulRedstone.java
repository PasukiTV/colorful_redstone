package de.pasuki.colorful_redstone;

import de.pasuki.colorful_redstone.registry.ModBlockEntityCompat;
import de.pasuki.colorful_redstone.registry.ModBlocks;
import de.pasuki.colorful_redstone.registry.ModCreativeTabs;
import de.pasuki.colorful_redstone.registry.ModItems;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.resources.ResourceLocation;

public final class ColorfulRedstone {
    public static final String MOD_ID = "colorful_redstone";

    private ColorfulRedstone() {
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void init() {
        ModBlocks.register();
        ModItems.register();

        // Run registry-dependent hooks after all registries are ready.
        LifecycleEvent.SETUP.register(() -> {
            ModBlockEntityCompat.register();
            ModCreativeTabs.register();
        });
    }
}
