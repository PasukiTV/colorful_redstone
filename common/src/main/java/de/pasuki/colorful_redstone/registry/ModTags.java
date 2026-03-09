package de.pasuki.colorful_redstone.registry;

import de.pasuki.colorful_redstone.ColorfulRedstone;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ModTags {
    private ModTags() {
    }

    public static final class Blocks {
        public static final TagKey<Block> COLORED_REDSTONE_WIRES = TagKey.create(Registries.BLOCK, ColorfulRedstone.id("colored_redstone_wires"));

        private Blocks() {
        }
    }

    public static final class Items {
        public static final TagKey<Item> COLORED_REDSTONE_WIRES = TagKey.create(Registries.ITEM, ColorfulRedstone.id("colored_redstone_wires"));

        private Items() {
        }
    }
}
