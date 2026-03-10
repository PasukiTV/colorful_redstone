package de.pasuki.colorful_redstone.neoforge;

import de.pasuki.colorful_redstone.ColorfulRedstone;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(ColorfulRedstone.MOD_ID)
public final class ColorfulRedstoneNeoForge {
    public ColorfulRedstoneNeoForge(IEventBus modBus) {
        // Run common setup.
        ColorfulRedstone.init();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ColorfulRedstoneNeoForgeClient.register(modBus);
        }
    }
}
