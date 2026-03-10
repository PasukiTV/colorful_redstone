package de.pasuki.colorful_redstone.neoforge;

import de.pasuki.colorful_redstone.block.ColoredRedstoneWireBlock;
import de.pasuki.colorful_redstone.registry.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public final class ColorfulRedstoneNeoForgeClient {
    private ColorfulRedstoneNeoForgeClient() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(ColorfulRedstoneNeoForgeClient::onClientSetup);
        modBus.addListener(ColorfulRedstoneNeoForgeClient::onRegisterBlockColors);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ModBlocks.COLORED_REDSTONE_WIRES.values()
                    .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout()));
            ModBlocks.COLORED_REDSTONE_TORCHES.values()
                    .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout()));
            ModBlocks.COLORED_REDSTONE_WALL_TORCHES.values()
                    .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout()));
            ModBlocks.COLORED_REDSTONE_REPEATERS.values()
                    .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout()));
            ModBlocks.COLORED_REDSTONE_COMPARATORS.values()
                    .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout()));
        });
    }

    private static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        Block[] blocks = ModBlocks.COLORED_REDSTONE_WIRES.values().stream()
                .map(supplier -> (Block) supplier.get())
                .toArray(Block[]::new);

        event.register((state, level, pos, tintIndex) -> {
            if (tintIndex != 0 || !(state.getBlock() instanceof ColoredRedstoneWireBlock coloredWire)) {
                return -1;
            }
            return coloredWire.getRenderColor(state.getValue(RedStoneWireBlock.POWER));
        }, blocks);
    }
}
