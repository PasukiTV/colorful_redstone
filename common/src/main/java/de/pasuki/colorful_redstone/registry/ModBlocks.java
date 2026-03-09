package de.pasuki.colorful_redstone.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import de.pasuki.colorful_redstone.ColorfulRedstone;
import de.pasuki.colorful_redstone.block.ColoredRedstoneWireBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ColorfulRedstone.MOD_ID, Registries.BLOCK);
    public static final Map<DyeColor, RegistrySupplier<Block>> COLORED_REDSTONE_WIRES = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Block>> COLORED_REDSTONE_BLOCKS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Block>> COLORED_REDSTONE_TORCHES = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistrySupplier<Block>> COLORED_REDSTONE_WALL_TORCHES = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            COLORED_REDSTONE_WIRES.put(color, BLOCKS.register(stoneDustId(color), () ->
                    new ColoredRedstoneWireBlock(color, BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_WIRE))));

            COLORED_REDSTONE_BLOCKS.put(color, BLOCKS.register(stoneBlockId(color), () ->
                    new PoweredBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK))));

            COLORED_REDSTONE_TORCHES.put(color, BLOCKS.register(stoneTorchId(color), () ->
                    new RedstoneTorchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_TORCH))));

            COLORED_REDSTONE_WALL_TORCHES.put(color, BLOCKS.register(stoneWallTorchId(color), () ->
                    new RedstoneWallTorchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_WALL_TORCH))));
        }
    }

    private ModBlocks() {
    }

    public static String stoneDustId(DyeColor color) {
        return color.getName().toLowerCase(Locale.ROOT) + "stone_dust";
    }

    public static String stoneBlockId(DyeColor color) {
        return color.getName().toLowerCase(Locale.ROOT) + "stone_block";
    }

    public static String stoneTorchId(DyeColor color) {
        return color.getName().toLowerCase(Locale.ROOT) + "stone_torch";
    }

    public static String stoneWallTorchId(DyeColor color) {
        return color.getName().toLowerCase(Locale.ROOT) + "stone_wall_torch";
    }

    public static void register() {
        BLOCKS.register();
    }
}
