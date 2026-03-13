package de.pasuki.colorful_redstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;

final class ColoredSignalUtil {
    private ColoredSignalUtil() {
    }

    static boolean canPowerTarget(BlockGetter level, BlockPos sourcePos, Direction direction, DyeColor color) {
        BlockPos targetPos = sourcePos.relative(direction);
        BlockState targetState = level.getBlockState(targetPos);

        // Non-redstone targets (piston, dispenser, observer, etc.) should be powered normally.
        if (!isAnyRedstoneComponent(targetState)) {
            return true;
        }

        // Redstone components are color-gated.
        return isSameColorComponent(targetState, color);
    }

    static boolean isSameColorComponent(BlockState state, DyeColor color) {
        if (state.getBlock() instanceof ColoredRedstoneWireBlock wire) {
            return wire.getColor() == color;
        }
        if (state.getBlock() instanceof ColoredRedstoneTorchBlock torch) {
            return torch.getColor() == color;
        }
        if (state.getBlock() instanceof ColoredRedstoneWallTorchBlock wallTorch) {
            return wallTorch.getColor() == color;
        }
        if (state.getBlock() instanceof ColoredRedstoneBlock block) {
            return block.getColor() == color;
        }
        if (state.getBlock() instanceof ColoredRepeaterBlock repeater) {
            return repeater.getColor() == color;
        }
        if (state.getBlock() instanceof ColoredComparatorBlock comparator) {
            return comparator.getColor() == color;
        }
        return false;
    }

    static boolean isAnyRedstoneComponent(BlockState state) {
        return state.getBlock() instanceof RedStoneWireBlock
                || state.is(Blocks.REDSTONE_BLOCK)
                || state.getBlock() instanceof RedstoneTorchBlock
                || state.getBlock() instanceof RedstoneWallTorchBlock
                || state.getBlock() instanceof RepeaterBlock
                || state.getBlock() instanceof ComparatorBlock
                || state.getBlock() instanceof ColoredRedstoneBlock;
    }
}
