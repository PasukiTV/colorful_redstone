package de.pasuki.colorful_redstone.neoforge;

import net.neoforged.fml.common.Mod;

import de.pasuki.colorful_redstone.ColorfulRedstone;

@Mod(ColorfulRedstone.MOD_ID)
public final class ColorfulRedstoneNeoForge {
    public ColorfulRedstoneNeoForge() {
        // Run our common setup.
        ColorfulRedstone.init();
    }
}
