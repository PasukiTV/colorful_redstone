package de.pasuki.colorful_redstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class ColoredRedstoneWallTorchBlock extends RedstoneWallTorchBlock {
    private static final float PARTICLE_CHANCE = 0.2F;

    private final DyeColor color;
    private final DustParticleOptions particle;

    public ColoredRedstoneWallTorchBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = color;
        this.particle = createParticle(color);
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

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT) || random.nextFloat() >= PARTICLE_CHANCE) {
            return;
        }

        Direction facing = state.getValue(FACING);
        Direction opposite = facing.getOpposite();

        double x = pos.getX() + 0.5D + 0.27D * opposite.getStepX() + (random.nextDouble() - 0.5D) * 0.2D;
        double y = pos.getY() + 0.92D + (random.nextDouble() - 0.5D) * 0.2D;
        double z = pos.getZ() + 0.5D + 0.27D * opposite.getStepZ() + (random.nextDouble() - 0.5D) * 0.2D;
        level.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    private static DustParticleOptions createParticle(DyeColor color) {
        int rgb = color.getTextColor();
        float red = ((rgb >> 16) & 255) / 255.0F;
        float green = ((rgb >> 8) & 255) / 255.0F;
        float blue = (rgb & 255) / 255.0F;
        return new DustParticleOptions(new Vector3f(red, green, blue), 1.0F);
    }
}
