package mc.oceanica.module.reef.world;

import mc.oceanica.Oceanica;
import mc.oceanica.module.reef.ReefModule;
import mc.oceanica.module.reef.block.BlockCoral;
import mc.oceanica.module.reef.block.BlockReefStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static mc.oceanica.module.reef.ReefModule.CORAL;


public class ReefWorldGenerator implements IWorldGenerator {
//TODO: Warning for the c0nfiguration

    private static final double REEF_SEED_FREQUENCY=.05;
    private static final double SEED_DENSITY = 0.55;
    private static final double DENSITY_DECREASE = 0.25;
    private static final double BASE_DECREASE_FACTOR = 1.15;

    private static final double MAX_LEVEL = 60;
    private static final double MIN_LEVEL = 30;

    private static List SPAWNABLE_BLOCKS=Arrays.asList(Blocks.DIRT,Blocks.GRAVEL, Blocks.SAND);

    //TODO: tweak generation
    //TODO: Density Bias according to the depth
    //TODO: Type Bias according to the depth and biome
    //TODO: Reduce spread of secondary.
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

        if (Oceanica.GENERATE_REEF) {
            Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);

            if (isSeedChunk(random,world,chunkX,chunkZ)) {
                Oceanica.logger.info("Generating Seed Chunk at: " +chunkX + " " + chunkZ);
                generateReefInChunk(random, world, chunkX, chunkZ, SEED_DENSITY);
                generateReefInChunk(random, world, chunkX, chunkZ+1, SEED_DENSITY-DENSITY_DECREASE);
                generateReefInChunk(random, world, chunkX, chunkZ-1, SEED_DENSITY-DENSITY_DECREASE);
                generateReefInChunk(random, world, chunkX+1, chunkZ, SEED_DENSITY-DENSITY_DECREASE);
                generateReefInChunk(random, world, chunkX+1, chunkZ+1, SEED_DENSITY-DENSITY_DECREASE);
                generateReefInChunk(random, world, chunkX+1, chunkZ-1, SEED_DENSITY-DENSITY_DECREASE);
                generateReefInChunk(random, world, chunkX-1, chunkZ, SEED_DENSITY-DENSITY_DECREASE);
                generateReefInChunk(random, world, chunkX-1, chunkZ+1, SEED_DENSITY-DENSITY_DECREASE);
                generateReefInChunk(random, world, chunkX-1, chunkZ-1, SEED_DENSITY-DENSITY_DECREASE);
                Oceanica.logger.info("End Generating Seed Chunk at: " +chunkX + " " + chunkZ);
            } else if (isSecondaryChunk(world, chunkX, chunkZ)) {
                double decreaseFactor= BASE_DECREASE_FACTOR +random.nextDouble();
                double density=decreaseFactor * DENSITY_DECREASE;
//                Oceanica.logger.info("\t\tDecrease Factor: " +decreaseFactor + " Density Decrease" + density);

                if (SEED_DENSITY > density) {
                    generateReefInChunk(random, world, chunkX, chunkZ, SEED_DENSITY - density);
                }
            }
        }
    }

    private void generateReefInChunk(Random random, World world, int chunkX, int chunkZ, double density) {
        Oceanica.logger.debug("\t\tGenerating at: " +chunkX + " " + chunkZ + " density " + density);

        int x = chunkX << 4;
        int z = chunkZ << 4;

        for(int xOffset=0; xOffset<16; xOffset++) {
            for(int zOffset=0; zOffset<16; zOffset++) {
                if (random.nextDouble() < density) {
                    BlockPos topBlock = world.getTopSolidOrLiquidBlock(getPosByFacing(x+xOffset, z, EnumFacing.WEST, zOffset)).down();
                    if (canSpawnInBlock(world,topBlock) && getReefStone() !=null) {
                        world.setBlockState(topBlock, getReefStone().getDefaultState(), 2 | 16);
                        if (random.nextDouble()<.7d) {
                            IBlockState state = CORAL.getDefaultState().withProperty(BlockCoral.CORAL_TYPE, EnumDyeColor.byMetadata(random.nextInt(15)));
                            world.setBlockState(topBlock.up(),state, 2 | 16);
                        }
                    }
                }
            }
        }
    }

    private BlockReefStone getReefStone() {
        return ReefModule.REEF_STONE;
    }

    private boolean canSpawnInBlock(World world, BlockPos pos) {
        IBlockState topBlock = world.getBlockState(pos);
        boolean isLevel=checkLevel(pos.getY());
        boolean isBlock=checkBlockType(topBlock);
        boolean isTopBlock=checkTopBlockType(world.getBlockState(pos.up()));
        boolean isBiome= BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.OCEAN) ||
                BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.BEACH);

        return isLevel && isBlock && isTopBlock && isBiome;
    }

    private boolean checkTopBlockType(IBlockState block) {
        return block.getBlock().equals(Blocks.WATER);
    }

    private boolean checkBlockType(IBlockState block) {
        return block.getMaterial().equals(Material.SAND) || SPAWNABLE_BLOCKS.contains(block.getBlock());
    }

    private boolean checkLevel(int y) {
        return y>=MIN_LEVEL && y<=MAX_LEVEL;
    }


    private boolean isSeedChunk(Random random, World world, int chunkX, int chunkZ) {
        return random.nextDouble() <=REEF_SEED_FREQUENCY;
//        return (chunkX%100==0 && chunkZ%20==0);
    }

    private boolean isSecondaryChunk(World world, int chunkX, int chunkZ) {
        Chunk chunk = world.getChunkFromChunkCoords(chunkX , chunkZ);

        boolean east=checkEastChunk(world, chunkX, chunkZ);
        boolean west=checkWestChunk(world, chunkX, chunkZ);
        boolean north=checkNorthChunk(world, chunkX, chunkZ);
        boolean south=checkSouthChunk(world, chunkX, chunkZ);

        return east || west || north || south;
    }

    private boolean checkNorthChunk(World world, int chunkX, int chunkZ) {
        return checkSecondaryChunk(world, chunkX, chunkZ, 0, -1, 0, 15, EnumFacing.NORTH);
    }

    private boolean checkSouthChunk(World world, int chunkX, int chunkZ) {
        return checkSecondaryChunk(world, chunkX, chunkZ, 0, 1, 0, 0, EnumFacing.SOUTH);
    }

    private boolean checkEastChunk(World world, int chunkX, int chunkZ) {
        return checkSecondaryChunk(world, chunkX, chunkZ, 1, 0, 0, 0, EnumFacing.EAST);
    }

    private boolean checkWestChunk(World world, int chunkX, int chunkZ) {
        return checkSecondaryChunk(world, chunkX, chunkZ, -1, 0, 15, 0, EnumFacing.WEST);
    }

    private boolean checkSecondaryChunk(World world, int chunkX, int chunkZ, int chunkXOffset, int chunkZOffset, int xOffset, int zOffset, EnumFacing facing) {
        boolean hasReef=false;

        if (world.isChunkGeneratedAt((chunkX +chunkXOffset), (chunkZ+chunkZOffset))) {
            Chunk chunk = world.getChunkFromChunkCoords(chunkX +chunkXOffset, chunkZ+chunkZOffset);

            int x = (chunk.xPosition << 4) + xOffset;
            int z = (chunk.zPosition << 4) + zOffset;
            for (int offset=0;offset<16 && !hasReef;offset++) {
                BlockPos pos = getPosByFacing(x, z, facing, offset);
                BlockPos blockToCheck = world.getTopSolidOrLiquidBlock(pos).down();
                hasReef = hasReef || world.getBlockState(blockToCheck).getBlock().equals(getReefStone());
            }
        }

        return hasReef;
    }




    private BlockPos getPosByFacing(int x, int z, EnumFacing facing, int offset) {

        switch (facing) {
            case EAST:
            case WEST:
                return new BlockPos(x, 1, z + offset);
            case NORTH:
            case SOUTH:
                return new BlockPos(x+offset, 1, z );
            default:
                throw new IllegalArgumentException("Illegal facing " + facing);
        }

    }

}
