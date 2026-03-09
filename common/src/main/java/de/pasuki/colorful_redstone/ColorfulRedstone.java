package de.pasuki.colorful_redstone;

import de.pasuki.colorful_redstone.registry.ModBlocks;
import de.pasuki.colorful_redstone.registry.ModCreativeTabs;
import de.pasuki.colorful_redstone.registry.ModItems;

public final class ColorfulRedstone {
    public static final String MOD_ID = "colorful_redstone";

    public static void init() {
        ModBlocks.register();
        ModItems.register();
        ModCreativeTabs.register();
    }
}
