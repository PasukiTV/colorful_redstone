package de.pasuki.colorful_redstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ColoredComparatorBlock extends ComparatorBlock {
    private final DyeColor color;

    public ColoredComparatorBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        Direction inputDirection = state.getValue(FACING).getOpposite();
        BlockPos inputPos = pos.relative(inputDirection);
        BlockState inputState = level.getBlockState(inputPos);

        if (ColoredSignalUtil.isDifferentColoredComponent(inputState, color)) {
            return 0;
        }

        return super.getInputSignal(level, pos, state);
    }

    @Override
    protected int getAlternateSignal(SignalGetter level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        Direction left = facing.getClockWise();
        Direction right = facing.getCounterClockWise();

        int leftSignal = getSideSignal(level, pos.relative(left), left);
        int rightSignal = getSideSignal(level, pos.relative(right), right);
        return Math.max(leftSignal, rightSignal);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Vanilla comparator does not emit redstone dust particles.
    }

    private int getSideSignal(SignalGetter level, BlockPos sidePos, Direction towardSide) {
        BlockState sideState = level.getBlockState(sidePos);

        if (ColoredSignalUtil.isDifferentColoredComponent(sideState, color)) {
            return 0;
        }

        int signal = sideState.getSignal(level, sidePos, towardSide);
        if (sideState.getBlock() instanceof RedStoneWireBlock) {
            signal = Math.max(signal, sideState.getValue(RedStoneWireBlock.POWER));
        }
        return signal;
    }
}
