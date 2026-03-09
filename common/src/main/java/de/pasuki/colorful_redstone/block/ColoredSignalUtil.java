package de.pasuki.colorful_redstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;

final class ColoredSignalUtil {
    private ColoredSignalUtil() {
    }

    static boolean canPowerTarget(BlockGetter level, BlockPos sourcePos, Direction direction, DyeColor color) {
        BlockPos targetPos = sourcePos.relative(direction);
        return level.getBlockState(targetPos).getBlock() instanceof ColoredRedstoneWireBlock wire
                && wire.getColor() == color;
    }
}
