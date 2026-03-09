package de.pasuki.colorful_redstone.registry;

import de.pasuki.colorful_redstone.mixin.BlockEntityTypeAccessor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ModBlockEntityCompat {
    private ModBlockEntityCompat() {
    }

    public static void register() {
        BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor) (Object) BlockEntityType.COMPARATOR;
        Set<Block> validBlocks = new HashSet<>(accessor.colorfulRedstone$getValidBlocks());

        for (Map.Entry<DyeColor, dev.architectury.registry.registries.RegistrySupplier<Block>> entry : ModBlocks.COLORED_REDSTONE_COMPARATORS.entrySet()) {
            validBlocks.add(entry.getValue().get());
        }

        accessor.colorfulRedstone$setValidBlocks(validBlocks);
    }
}
