package de.pasuki.colorful_redstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.PoweredBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ColoredRedstoneBlock extends PoweredBlock {
    private final DyeColor color;

    public ColoredRedstoneBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        int base = super.getSignal(state, level, pos, direction);
        if (base <= 0) {
            return 0;
        }
        return ColoredSignalUtil.canPowerTarget(level, pos, direction.getOpposite(), color) ? base : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        int base = super.getDirectSignal(state, level, pos, direction);
        if (base <= 0) {
            return 0;
        }
        return ColoredSignalUtil.canPowerTarget(level, pos, direction.getOpposite(), color) ? base : 0;
    }
}

