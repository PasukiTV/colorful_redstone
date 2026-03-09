package de.pasuki.colorful_redstone.fabric.client;

import de.pasuki.colorful_redstone.block.ColoredRedstoneWireBlock;
import de.pasuki.colorful_redstone.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class ColorfulRedstoneFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Block[] coloredWireBlocks = ModBlocks.COLORED_REDSTONE_WIRES.values().stream()
                .map(supplier -> (Block) supplier.get())
                .toArray(Block[]::new);
        Block[] coloredTorchBlocks = ModBlocks.COLORED_REDSTONE_TORCHES.values().stream()
                .map(supplier -> (Block) supplier.get())
                .toArray(Block[]::new);
        Block[] coloredWallTorchBlocks = ModBlocks.COLORED_REDSTONE_WALL_TORCHES.values().stream()
                .map(supplier -> (Block) supplier.get())
                .toArray(Block[]::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), coloredWireBlocks);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), coloredTorchBlocks);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), coloredWallTorchBlocks);

        ColorProviderRegistry.BLOCK.register((BlockState state, net.minecraft.world.level.BlockAndTintGetter level, net.minecraft.core.BlockPos pos, int tintIndex) -> {
            if (tintIndex != 0 || !(state.getBlock() instanceof ColoredRedstoneWireBlock coloredWire)) {
                return -1;
            }
            return coloredWire.getRenderColor(state.getValue(RedStoneWireBlock.POWER));
        }, coloredWireBlocks);
    }
}