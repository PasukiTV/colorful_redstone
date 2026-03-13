package de.pasuki.colorful_redstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class ColoredRepeaterBlock extends RepeaterBlock {
    private static final float PARTICLE_CHANCE = 0.2F;

    private final DyeColor color;
    private final DustParticleOptions poweredParticle;

    public ColoredRepeaterBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = color;
        this.poweredParticle = createParticle(color);
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
        if (!state.getValue(POWERED) || random.nextFloat() >= PARTICLE_CHANCE) {
            return;
        }

        Direction facing = state.getValue(FACING);
        double xOffset = random.nextBoolean() ? -0.12D : 0.12D;
        spawnRelative(level, pos, facing, random, poweredParticle, xOffset, -0.10D, 0.18D);
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

    private static DustParticleOptions createParticle(DyeColor color) {
        int rgb = color.getTextColor();
        float red = Math.max(0.2F, ((rgb >> 16) & 255) / 255.0F);
        float green = Math.max(0.2F, ((rgb >> 8) & 255) / 255.0F);
        float blue = Math.max(0.2F, (rgb & 255) / 255.0F);
        return new DustParticleOptions(new Vector3f(red, green, blue), 1.0F);
    }

    private static void spawnRelative(Level level, BlockPos pos, Direction facing, RandomSource random,
                                      DustParticleOptions particle, double relX, double relZ, double relY) {
        double rx;
        double rz;
        switch (facing) {
            case SOUTH -> {
                rx = relX;
                rz = relZ;
            }
            case WEST -> {
                rx = relZ;
                rz = -relX;
            }
            case NORTH -> {
                rx = -relX;
                rz = -relZ;
            }
            case EAST -> {
                rx = -relZ;
                rz = relX;
            }
            default -> {
                rx = relX;
                rz = relZ;
            }
        }

        double x = pos.getX() + 0.5D + rx + (random.nextDouble() - 0.5D) * 0.03D;
        double y = pos.getY() + relY + (random.nextDouble() - 0.5D) * 0.03D;
        double z = pos.getZ() + 0.5D + rz + (random.nextDouble() - 0.5D) * 0.03D;
        level.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
    }
}
