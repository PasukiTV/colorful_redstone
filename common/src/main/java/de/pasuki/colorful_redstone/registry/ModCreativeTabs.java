package de.pasuki.colorful_redstone.registry;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.world.item.CreativeModeTabs;

public final class ModCreativeTabs {
    private ModCreativeTabs() {
    }

    public static void register() {
        for (var item : ModItems.ITEMS) {
            CreativeTabRegistry.append(CreativeModeTabs.REDSTONE_BLOCKS, item.get());
        }
    }
}
