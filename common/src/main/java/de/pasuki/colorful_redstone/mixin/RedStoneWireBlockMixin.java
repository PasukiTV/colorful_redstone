package de.pasuki.colorful_redstone.mixin;

import de.pasuki.colorful_redstone.block.ColoredRedstoneWireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedStoneWireBlock.class)
public abstract class RedStoneWireBlockMixin {
    @Inject(method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z", at = @At("HEAD"), cancellable = true)
    private static void colorful_redstone$shouldConnectToColoredWires(BlockState state, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof ColoredRedstoneWireBlock) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;", at = @At("RETURN"), cancellable = true)
    private void colorful_redstone$disallowMixedWireConnections(BlockGetter level, BlockPos pos, Direction direction, boolean canConnectUp, CallbackInfoReturnable<RedstoneSide> cir) {
        if (!cir.getReturnValue().isConnected()) {
            return;
        }

        BlockState neighbor = level.getBlockState(pos.relative(direction));
        Object self = this;

        if (self instanceof ColoredRedstoneWireBlock currentColoredWire) {
            if (neighbor.is(Blocks.REDSTONE_WIRE)) {
                cir.setReturnValue(RedstoneSide.NONE);
                return;
            }

            if (neighbor.getBlock() instanceof ColoredRedstoneWireBlock neighborColoredWire
                    && neighborColoredWire.getColor() != currentColoredWire.getColor()) {
                cir.setReturnValue(RedstoneSide.NONE);
            }
            return;
        }

        if (neighbor.getBlock() instanceof ColoredRedstoneWireBlock) {
            cir.setReturnValue(RedstoneSide.NONE);
        }
    }
}
