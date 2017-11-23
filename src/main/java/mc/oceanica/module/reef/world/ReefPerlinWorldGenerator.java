package mc.oceanica.module.reef.world;

import mc.oceanica.OceanicaConfig;
import mc.oceanica.OceanicaStats;
import mc.oceanica.module.reef.ReefModule;
import mc.oceanica.module.reef.block.BlockCoral;
import mc.util.PerlinNoiseGen;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static mc.oceanica.module.reef.ReefModule.CORAL;

/**
 * Why do I use the Perlin Noise for everything instead of a simple density/chance check with Random?
 * I want predictability, so neighbouring chunks can be examined even if they are not created.
 * I may change the noise function in the future, just for fun.
 */
//TODO: Document.
//TODO: this "should" be configurable. maybe.
public class ReefPerlinWorldGenerator implements IWorldGenerator {
    public static final int REEF_NOISE_LAYER = 0;
    public static final int CORAL_NOISE_LAYER = 2;
    public static final double KELP_NOISE_LAYER = 4;
    public static final double KELP_HEIGHT_LAYER = 5;

    //TODO: this should be.. somewhere else?
    private static List SPAWNABLE_BLOCKS = Arrays.asList(Blocks.DIRT, Blocks.GRAVEL, Blocks.SAND);

    private PerlinNoiseGen noiseGen;
    //TODO: tweak generation
    //TODO: Type Bias according to the depth

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (OceanicaConfig.reef.generateReef) {
            OceanicaStats.INSTANCE.addChunkProcessed();
            if (noiseGen == null) noiseGen = new PerlinNoiseGen(world.getSeed());
            generateReefInChunk(random, world, chunkX, chunkZ);
        }
    }


    private void generateReefInChunk(Random random, World world, int chunkX, int chunkZ) {

        int x = chunkX << 4;
        int z = chunkZ << 4;

        double value = getNoiseForChunk(chunkX, chunkZ);

        if (value < OceanicaConfig.reef.procGen.kelpChunkThreshold) {
            OceanicaStats.INSTANCE.addKelpCount();
            generateKelpAtChunk(world, x, z);
            //TODO: smooth edges of kelp chunks
        } else if (value <= OceanicaConfig.reef.procGen.seedChunkThreshold) {
            //To have less coral chunks, add a new threshold where "nothing" happens.
            if (isPrimarySeedChunk(chunkX, chunkZ + 1) ||
                    isPrimarySeedChunk(chunkX, chunkZ - 1) ||
                    isPrimarySeedChunk(chunkX + 1, chunkZ) ||
                    isPrimarySeedChunk(chunkX + 1, chunkZ + 1) ||
                    isPrimarySeedChunk(chunkX + 1, chunkZ - 1) ||
                    isPrimarySeedChunk(chunkX - 1, chunkZ) ||
                    isPrimarySeedChunk(chunkX - 1, chunkZ + 1) ||
                    isPrimarySeedChunk(chunkX - 1, chunkZ - 1)) {
                OceanicaStats.INSTANCE.addSecondaryCount();
                generateCoralInChunk(random, world, x, z, OceanicaConfig.reef.procGen.secondaryReefDensityThreshold);
            }
        } else {
            OceanicaStats.INSTANCE.addPrimarySeed();
            generateCoralInChunk(random, world, x, z, OceanicaConfig.reef.procGen.primaryReefDensityThreshold);
        }
    }

    private boolean isPrimarySeedChunk(int chunkX, int chunkZ) {
        return noiseGen.getValue(chunkX / OceanicaConfig.reef.procGen.reefNoiseScale, REEF_NOISE_LAYER, chunkZ / OceanicaConfig.reef.procGen.reefNoiseScale) > OceanicaConfig.reef.procGen.seedChunkThreshold;
    }

    private void generateCoralInChunk(Random random, World world, int startX, int startZ, double densityThreshold) {
        for (int xOffset = 0; xOffset < 16; xOffset++) {
            for (int zOffset = 0; zOffset < 16; zOffset++) {
                double density = noiseGen.getValue(startX + xOffset, CORAL_NOISE_LAYER, startZ + zOffset);
                if (density > densityThreshold) {
                    addCoral(random, world, startX + xOffset, startZ + zOffset);
                }
            }
        }
    }

    private void generateKelpAtChunk(World world, int startX, int startZ) {
        for (int xOffset = 0; xOffset < 16; xOffset++) {
            for (int zOffset = 0; zOffset < 16; zOffset++) {
                double density = noiseGen.getValue(startX + xOffset, KELP_NOISE_LAYER, startZ + zOffset);
                if (density > OceanicaConfig.reef.procGen.kelpDensityThreshold) {
                    BlockPos blockPos = new BlockPos(startX + xOffset, 1, startZ + zOffset);
                    BlockPos topBlock = world.getTopSolidOrLiquidBlock(blockPos).down();

                    if (canSpawnInBlock(world, topBlock) && ReefModule.KELP != null) {
                        double heightFactor = noiseGen.getValue(startX + xOffset, KELP_HEIGHT_LAYER, startZ + zOffset);
                        int height = MathHelper.floor((world.getSeaLevel() - 1 - topBlock.getY()) * heightFactor);
                        if (height > 0) {
                            for (int i = 1; i <= height; i++) {
                                //TODO: change the state when the growth mechanism is implemented
                                world.setBlockState(topBlock.up(i), ReefModule.KELP.getDefaultState(), 2 | 16);
                            }
                        }
                    }
                }
            }
        }
    }

    private double getNoiseForChunk(int chunkX, int chunkZ) {
        return noiseGen.getValue(chunkX / OceanicaConfig.reef.procGen.reefNoiseScale, REEF_NOISE_LAYER, chunkZ / OceanicaConfig.reef.procGen.reefNoiseScale);
    }

    private void addCoral(Random random, World world, int x, int z) {
        BlockPos blockPos = new BlockPos(x, 1, z);
        BlockPos topBlock = world.getTopSolidOrLiquidBlock(blockPos).down();
        if (canSpawnInBlock(world, topBlock) && ReefModule.REEF_STONE != null) {
            world.setBlockState(topBlock, ReefModule.REEF_STONE.getDefaultState(), 2 | 16);
            if (random.nextDouble() < OceanicaConfig.reef.procGen.coralDensity) {
                IBlockState state = CORAL.getDefaultState().withProperty(BlockCoral.CORAL_TYPE, EnumDyeColor.byMetadata(random.nextInt(16)));
                world.setBlockState(topBlock.up(), state, 2 | 16);
            }
        }
    }

    private boolean canSpawnInBlock(World world, BlockPos pos) {
        IBlockState topBlock = world.getBlockState(pos);
        int y = pos.getY();

        boolean isLevel = y >= OceanicaConfig.reef.minLevel && y <= world.getSeaLevel() - OceanicaConfig.reef.maxLevelOffset;
        boolean isBlock = checkBlockType(topBlock);
        boolean isTopBlock = checkTopBlockType(world.getBlockState(pos.up()));
        boolean isBiome = BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.OCEAN) ||
                BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.BEACH);

        return isLevel && isBlock && isTopBlock && isBiome;
    }

    private boolean checkTopBlockType(IBlockState block) {
        return block.getBlock().equals(Blocks.WATER);
    }

    private boolean checkBlockType(IBlockState block) {
        return block.getMaterial().equals(Material.SAND) || SPAWNABLE_BLOCKS.contains(block.getBlock());
    }

}
