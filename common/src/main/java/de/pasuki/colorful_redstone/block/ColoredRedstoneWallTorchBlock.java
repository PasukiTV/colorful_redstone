package de.pasuki.colorful_redstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class ColoredRedstoneWallTorchBlock extends RedstoneWallTorchBlock {
    private final DyeColor color;

    public ColoredRedstoneWallTorchBlock(DyeColor color, BlockBehaviour.Properties properties) {
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
        return ColoredSignalUtil.canPowerTarget(level, pos, direction, color) ? base : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        int base = super.getDirectSignal(state, level, pos, direction);
        if (base <= 0) {
            return 0;
        }
        return ColoredSignalUtil.canPowerTarget(level, pos, direction, color) ? base : 0;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        Direction facing = state.getValue(FACING);
        Direction opposite = facing.getOpposite();

        int textColor = color.getTextColor();
        int red = (textColor >> 16) & 255;
        int green = (textColor >> 8) & 255;
        int blue = textColor & 255;

        int rgb = FastColor.ARGB32.color(255, red, green, blue);
        float pr = ((rgb >> 16) & 255) / 255.0F;
        float pg = ((rgb >> 8) & 255) / 255.0F;
        float pb = (rgb & 255) / 255.0F;
        DustParticleOptions particle = new DustParticleOptions(new Vector3f(pr, pg, pb), 1.0F);

        double x = pos.getX() + 0.5D + 0.27D * opposite.getStepX() + (random.nextDouble() - 0.5D) * 0.2D;
        double y = pos.getY() + 0.92D + (random.nextDouble() - 0.5D) * 0.2D;
        double z = pos.getZ() + 0.5D + 0.27D * opposite.getStepZ() + (random.nextDouble() - 0.5D) * 0.2D;
        level.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
    }
}