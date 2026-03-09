package de.pasuki.colorful_redstone.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import de.pasuki.colorful_redstone.ColorfulRedstone;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ColorfulRedstone.MOD_ID, Registries.ITEM);

    static {
        for (Map.Entry<DyeColor, RegistrySupplier<Block>> entry : ModBlocks.COLORED_REDSTONE_WIRES.entrySet()) {
            DyeColor color = entry.getKey();
            RegistrySupplier<Block> block = entry.getValue();
            ITEMS.register(ModBlocks.stoneDustId(color), () -> new ItemNameBlockItem(block.get(), new Item.Properties()));
        }

        for (Map.Entry<DyeColor, RegistrySupplier<Block>> entry : ModBlocks.COLORED_REDSTONE_BLOCKS.entrySet()) {
            DyeColor color = entry.getKey();
            RegistrySupplier<Block> block = entry.getValue();
            ITEMS.register(ModBlocks.stoneBlockId(color), () -> new ItemNameBlockItem(block.get(), new Item.Properties()));
        }

        for (Map.Entry<DyeColor, RegistrySupplier<Block>> entry : ModBlocks.COLORED_REDSTONE_TORCHES.entrySet()) {
            DyeColor color = entry.getKey();
            RegistrySupplier<Block> standingTorch = entry.getValue();
            RegistrySupplier<Block> wallTorch = ModBlocks.COLORED_REDSTONE_WALL_TORCHES.get(color);
            ITEMS.register(ModBlocks.stoneTorchId(color), () ->
                    new StandingAndWallBlockItem(standingTorch.get(), wallTorch.get(), new Item.Properties(), Direction.DOWN));
        }

        for (Map.Entry<DyeColor, RegistrySupplier<Block>> entry : ModBlocks.COLORED_REDSTONE_REPEATERS.entrySet()) {
            DyeColor color = entry.getKey();
            RegistrySupplier<Block> block = entry.getValue();
            ITEMS.register(ModBlocks.stoneRepeaterId(color), () -> new ItemNameBlockItem(block.get(), new Item.Properties()));
        }

        for (Map.Entry<DyeColor, RegistrySupplier<Block>> entry : ModBlocks.COLORED_REDSTONE_COMPARATORS.entrySet()) {
            DyeColor color = entry.getKey();
            RegistrySupplier<Block> block = entry.getValue();
            ITEMS.register(ModBlocks.stoneComparatorId(color), () -> new ItemNameBlockItem(block.get(), new Item.Properties()));
        }
    }

    private ModItems() {
    }

    public static void register() {
        ITEMS.register();
    }
}
