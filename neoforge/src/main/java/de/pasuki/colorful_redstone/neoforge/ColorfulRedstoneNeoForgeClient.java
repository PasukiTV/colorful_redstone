package de.pasuki.colorful_redstone.neoforge;

import de.pasuki.colorful_redstone.ColorfulRedstone;
import de.pasuki.colorful_redstone.block.ColoredRedstoneWireBlock;
import de.pasuki.colorful_redstone.registry.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = ColorfulRedstone.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ColorfulRedstoneNeoForgeClient {
    private ColorfulRedstoneNeoForgeClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ModBlocks.COLORED_REDSTONE_WIRES.values()
                    .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout()));
            ModBlocks.COLORED_REDSTONE_TORCHES.values()
                    .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout()));
            ModBlocks.COLORED_REDSTONE_WALL_TORCHES.values()
                    .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout()));
        });
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
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