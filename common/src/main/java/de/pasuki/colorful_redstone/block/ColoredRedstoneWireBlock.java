package de.pasuki.colorful_redstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import org.joml.Vector3f;

public class ColoredRedstoneWireBlock extends RedStoneWireBlock {
    private static final float CENTER_PARTICLE_CHANCE = 0.1F;
    private static final float SIDE_PARTICLE_CHANCE = 0.1F;

    private final DyeColor color;

    public ColoredRedstoneWireBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    public int getRenderColor(int power) {
        float strength = Math.max(0.0F, power / 15.0F);
        float brightness = 0.45F + (0.55F * strength);

        int rgb = color.getTextColor();
        int red = (int) (((rgb >> 16) & 255) * brightness);
        int green = (int) (((rgb >> 8) & 255) * brightness);
        int blue = (int) ((rgb & 255) * brightness);

        red = Math.min(255, Math.max(0, red));
        green = Math.min(255, Math.max(0, green));
        blue = Math.min(255, Math.max(0, blue));

        return FastColor.ARGB32.color(255, red, green, blue);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int power = state.getValue(POWER);
        if (power <= 0) {
            return;
        }

        int rgb = getRenderColor(power);
        float red = ((rgb >> 16) & 255) / 255.0F;
        float green = ((rgb >> 8) & 255) / 255.0F;
        float blue = (rgb & 255) / 255.0F;
        DustParticleOptions particle = new DustParticleOptions(new Vector3f(red, green, blue), 1.0F);

        double centerX = pos.getX() + 0.5D;
        double centerY = pos.getY() + 0.0625D;
        double centerZ = pos.getZ() + 0.5D;

        if (random.nextFloat() < CENTER_PARTICLE_CHANCE) {
            level.addParticle(particle,
                    centerX + (random.nextDouble() - 0.5D) * 0.2D,
                    centerY,
                    centerZ + (random.nextDouble() - 0.5D) * 0.2D,
                    0.0D, 0.0D, 0.0D);
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide side = state.getValue(PROPERTY_BY_DIRECTION.get(direction));
            if (!side.isConnected() || random.nextFloat() >= SIDE_PARTICLE_CHANCE) {
                continue;
            }

            double lineOffset = 0.3D;
            double x = centerX + direction.getStepX() * lineOffset + (random.nextDouble() - 0.5D) * 0.08D;
            double z = centerZ + direction.getStepZ() * lineOffset + (random.nextDouble() - 0.5D) * 0.08D;
            double y = centerY + (side == RedstoneSide.UP ? 0.18D : 0.0D);

            level.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
