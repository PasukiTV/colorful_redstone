package de.pasuki.colorful_redstone.mixin;

import de.pasuki.colorful_redstone.block.ColoredComparatorBlock;
import de.pasuki.colorful_redstone.block.ColoredRedstoneBlock;
import de.pasuki.colorful_redstone.block.ColoredRedstoneTorchBlock;
import de.pasuki.colorful_redstone.block.ColoredRedstoneWallTorchBlock;
import de.pasuki.colorful_redstone.block.ColoredRedstoneWireBlock;
import de.pasuki.colorful_redstone.block.ColoredRepeaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedStoneWireBlock.class)
public abstract class RedStoneWireBlockMixin {
    @Shadow
    private boolean shouldSignal;

    @Shadow
    private static boolean shouldConnectTo(BlockState state, Direction direction) {
        throw new AssertionError();
    }

    private static boolean colorful_redstone$canClimb(BlockState state, BlockGetter level, BlockPos pos) {
        return state.isFaceSturdy(level, pos, Direction.UP);
    }

    @Inject(method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z", at = @At("HEAD"), cancellable = true)
    private static void colorful_redstone$shouldConnectToColoredWires(BlockState state, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof ColoredRedstoneWireBlock) {
            cir.setReturnValue(true);
            return;
        }

        if (state.getBlock() instanceof ColoredRepeaterBlock) {
            if (direction == null) {
                cir.setReturnValue(false);
                return;
            }

            Direction facing = state.getValue(RepeaterBlock.FACING);
            cir.setReturnValue(facing == direction || facing.getOpposite() == direction);
            return;
        }

        if (state.getBlock() instanceof ColoredComparatorBlock) {
            cir.setReturnValue(direction != null);
        }
    }

    @Inject(method = "getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;", at = @At("RETURN"), cancellable = true)
    private void colorful_redstone$disallowMixedWireConnections(BlockGetter level, BlockPos pos, Direction direction, boolean canConnectUp, CallbackInfoReturnable<RedstoneSide> cir) {
        BlockPos neighborPos = pos.relative(direction);
        BlockState neighbor = level.getBlockState(neighborPos);
        Object self = this;

        if (!(self instanceof ColoredRedstoneWireBlock currentColoredWire)) {
            return;
        }

        if (neighbor.getBlock() instanceof ColoredRedstoneWireBlock neighborColoredWire) {
            if (neighborColoredWire.getColor() != currentColoredWire.getColor()) {
                cir.setReturnValue(RedstoneSide.NONE);
            }
            return;
        }

        if (neighbor.getBlock() instanceof ColoredRedstoneTorchBlock coloredTorch
                && coloredTorch.getColor() != currentColoredWire.getColor()) {
            cir.setReturnValue(RedstoneSide.NONE);
            return;
        }
        if (neighbor.getBlock() instanceof ColoredRedstoneWallTorchBlock coloredWallTorch
                && coloredWallTorch.getColor() != currentColoredWire.getColor()) {
            cir.setReturnValue(RedstoneSide.NONE);
            return;
        }

        if (neighbor.getBlock() instanceof ColoredRepeaterBlock coloredRepeater
                && coloredRepeater.getColor() != currentColoredWire.getColor()) {
            cir.setReturnValue(RedstoneSide.NONE);
            return;
        }
        if (neighbor.getBlock() instanceof ColoredComparatorBlock coloredComparator
                && coloredComparator.getColor() != currentColoredWire.getColor()) {
            cir.setReturnValue(RedstoneSide.NONE);
            return;
        }

        if (neighbor.getBlock() instanceof ColoredRedstoneBlock coloredBlock) {
            if (coloredBlock.getColor() != currentColoredWire.getColor()) {
                cir.setReturnValue(RedstoneSide.NONE);
                return;
            }

            if (canConnectUp && colorful_redstone$canClimb(neighbor, level, neighborPos)) {
                BlockState aboveNeighbor = level.getBlockState(neighborPos.above());
                if (aboveNeighbor.getBlock() instanceof ColoredRedstoneWireBlock upWire
                        && upWire.getColor() == currentColoredWire.getColor()) {
                    cir.setReturnValue(RedstoneSide.UP);
                    return;
                }
            }

            cir.setReturnValue(RedstoneSide.SIDE);
            return;
        }

        if (canConnectUp && colorful_redstone$canClimb(neighbor, level, neighborPos)) {
            BlockState aboveNeighbor = level.getBlockState(neighborPos.above());
            if (aboveNeighbor.getBlock() instanceof ColoredRedstoneWireBlock upWire
                    && upWire.getColor() == currentColoredWire.getColor()) {
                cir.setReturnValue(RedstoneSide.UP);
            }
        }
    }

    @Inject(method = "calculateTargetStrength(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I", at = @At("HEAD"), cancellable = true)
    private void colorful_redstone$onlyAcceptSameColorInputs(Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (!((Object) this instanceof ColoredRedstoneWireBlock currentColoredWire)) {
            return;
        }

        int maxExternalSignal = 0;
        boolean previousShouldSignal = this.shouldSignal;
        this.shouldSignal = false;
        try {
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.relative(direction);
                BlockState neighborState = level.getBlockState(neighborPos);

                // Wires are handled in the wire-decay pass below, never as direct external power.
                if (neighborState.getBlock() instanceof RedStoneWireBlock) {
                    continue;
                }

                if (neighborState.getBlock() instanceof ColoredRedstoneBlock coloredBlock
                        && coloredBlock.getColor() != currentColoredWire.getColor()) {
                    continue;
                }
                if (neighborState.getBlock() instanceof ColoredRedstoneTorchBlock coloredTorch
                        && coloredTorch.getColor() != currentColoredWire.getColor()) {
                    continue;
                }
                if (neighborState.getBlock() instanceof ColoredRedstoneWallTorchBlock coloredWallTorch
                        && coloredWallTorch.getColor() != currentColoredWire.getColor()) {
                    continue;
                }
                if (neighborState.getBlock() instanceof ColoredRepeaterBlock coloredRepeater
                        && (coloredRepeater.getColor() != currentColoredWire.getColor()
                        || !shouldConnectTo(neighborState, direction))) {
                    continue;
                }
                if (neighborState.getBlock() instanceof ColoredComparatorBlock coloredComparator
                        && (coloredComparator.getColor() != currentColoredWire.getColor()
                        || !shouldConnectTo(neighborState, direction))) {
                    continue;
                }

                int signal = level.getSignal(neighborPos, direction.getOpposite());
                if (signal > maxExternalSignal) {
                    maxExternalSignal = signal;
                }
            }
        } finally {
            this.shouldSignal = previousShouldSignal;
        }

        int maxWirePower = 0;
        BlockPos above = pos.above();
        boolean aboveBlocksUpPath = colorful_redstone$canClimb(level.getBlockState(above), level, above);

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos sidePos = pos.relative(direction);
            BlockState sideState = level.getBlockState(sidePos);

            if (sideState.getBlock() instanceof RedStoneWireBlock) {
                if (!(sideState.getBlock() instanceof ColoredRedstoneWireBlock sideWire)
                        || sideWire.getColor() == currentColoredWire.getColor()) {
                    maxWirePower = Math.max(maxWirePower, sideState.getValue(RedStoneWireBlock.POWER));
                }
            }

            boolean sideCanClimb = colorful_redstone$canClimb(sideState, level, sidePos);
            if (sideCanClimb && !aboveBlocksUpPath) {
                BlockPos upPos = sidePos.above();
                BlockState upState = level.getBlockState(upPos);
                if (upState.getBlock() instanceof RedStoneWireBlock) {
                    if (!(upState.getBlock() instanceof ColoredRedstoneWireBlock upWire)
                            || upWire.getColor() == currentColoredWire.getColor()) {
                        maxWirePower = Math.max(maxWirePower, upState.getValue(RedStoneWireBlock.POWER));
                    }
                }
            } else if (!sideCanClimb) {
                BlockPos downPos = sidePos.below();
                BlockState downState = level.getBlockState(downPos);
                if (downState.getBlock() instanceof RedStoneWireBlock) {
                    if (!(downState.getBlock() instanceof ColoredRedstoneWireBlock downWire)
                            || downWire.getColor() == currentColoredWire.getColor()) {
                        maxWirePower = Math.max(maxWirePower, downState.getValue(RedStoneWireBlock.POWER));
                    }
                }
            }
        }

        int fromWires = Math.max(0, maxWirePower - 1);
        cir.setReturnValue(Math.max(maxExternalSignal, fromWires));
    }
}
