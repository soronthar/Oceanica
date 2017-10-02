package mc.oceanica.module.reef.world;

import mc.oceanica.Oceanica;
import mc.oceanica.OceanicaConfig;
import mc.oceanica.OceanicaStats;
import mc.oceanica.module.reef.ReefModule;
import mc.oceanica.module.reef.block.BlockCoral;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static mc.oceanica.module.reef.ReefModule.CORAL;


public class ReefWorldGenerator implements IWorldGenerator {
    private static final double MIN_LEVEL = 5;

    private static List SPAWNABLE_BLOCKS=Arrays.asList(Blocks.DIRT,Blocks.GRAVEL, Blocks.SAND);

    //TODO: tweak generation
    //TODO: Density Bias according to the depth
    //TODO: Type Bias according to the depth

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        OceanicaStats.INSTANCE.addChunkProcessed();
        if (OceanicaConfig.generateReef) {
            Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);

            if (isSeedChunk(random,world,chunkX,chunkZ)) {
                OceanicaStats.INSTANCE.addPrimarySeed();

                if (OceanicaConfig.isDebug) {
                    generateDebugRing(chunkX, chunkZ, world);
                }

                generateReefInChunk(random, world, chunkX, chunkZ, OceanicaConfig.reefStoneDensity);

                if (OceanicaConfig.enableSecondarySeedChunk) {
                    double reducedDensity = OceanicaConfig.reefStoneDensity - OceanicaConfig.densityDecrease;
                    generateReefInChunk(random, world, chunkX, chunkZ+1, reducedDensity);
                    generateReefInChunk(random, world, chunkX, chunkZ-1, reducedDensity);
                    generateReefInChunk(random, world, chunkX+1, chunkZ, reducedDensity);
                    generateReefInChunk(random, world, chunkX+1, chunkZ+1, reducedDensity);
                    generateReefInChunk(random, world, chunkX+1, chunkZ-1, reducedDensity);
                    generateReefInChunk(random, world, chunkX-1, chunkZ, reducedDensity);
                    generateReefInChunk(random, world, chunkX-1, chunkZ+1, reducedDensity);
                    generateReefInChunk(random, world, chunkX-1, chunkZ-1, reducedDensity);
                }


            } else if (isSecondaryChunk(world, chunkX, chunkZ)) {
                double decreaseFactor= OceanicaConfig.baseDecreaseFactor +random.nextDouble();
                double density=decreaseFactor * OceanicaConfig.densityDecrease;
                double secondaryDensity = OceanicaConfig.reefStoneDensity - density;

                if (secondaryDensity>0) {
                    OceanicaStats.INSTANCE.addSecondaryCount();
                    generateReefInChunk(random, world, chunkX, chunkZ, secondaryDensity);
                }
            }
        }
    }

    private void generateDebugRing(int chunkX, int chunkZ, World world) {
        int x = chunkX << 4;
        int z = chunkZ << 4;
        int y=world.getSeaLevel() + 2;
        world.setBlockState(new BlockPos(x,y,z), ReefModule.REEF_STONE.getDefaultState(), 2 | 16);
        for (int i=1;i<16;i++) {
            IBlockState markerBlock = ReefModule.REEF_STONE.getDefaultState();
            world.setBlockState(new BlockPos(x+i,y,z), markerBlock, 2 | 16);
            world.setBlockState(new BlockPos(x,y,z+i), markerBlock, 2 | 16);
            world.setBlockState(new BlockPos(x+i,y,z+15), markerBlock, 2 | 16);
            world.setBlockState(new BlockPos(x+15,y,z+i), markerBlock, 2 | 16);
        }
    }

    private void generateReefInChunk(Random random, World world, int chunkX, int chunkZ, double density) {
//        Oceanica.logger.debug("\t\tGenerating at: " +chunkX + " " + chunkZ + " density " + density);

        int x = chunkX << 4;
        int z = chunkZ << 4;

        for(int xOffset=0; xOffset<16; xOffset++) {
            for(int zOffset=0; zOffset<16; zOffset++) {
                if (random.nextDouble() < density) {
                    BlockPos topBlock = world.getTopSolidOrLiquidBlock(getPosByFacing(x+xOffset, z, EnumFacing.WEST, zOffset)).down();
                    if (canSpawnInBlock(world,topBlock) && ReefModule.REEF_STONE !=null) {
                        world.setBlockState(topBlock, ReefModule.REEF_STONE.getDefaultState(), 2 | 16);
                        if (random.nextDouble()< OceanicaConfig.coralDensity) {
                            IBlockState state = CORAL.getDefaultState().withProperty(BlockCoral.CORAL_TYPE, EnumDyeColor.byMetadata(random.nextInt(16)));
                            world.setBlockState(topBlock.up(),state, 2 | 16);
                        }
                    }
                }
            }
        }
    }

    private boolean canSpawnInBlock(World world, BlockPos pos) {
        IBlockState topBlock = world.getBlockState(pos);
        int y = pos.getY();
        boolean isLevel= y >= MIN_LEVEL && y <= world.getSeaLevel()-2;
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


    private boolean isSeedChunk(Random random, World world, int chunkX, int chunkZ) {
        return random.nextDouble() <= OceanicaConfig.seedChunkFrequency;
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

            int x = (chunk.x << 4) + xOffset;
            int z = (chunk.z << 4) + zOffset;
            for (int offset=0;offset<16 && !hasReef;offset++) {
                BlockPos pos = getPosByFacing(x, z, facing, offset);
                BlockPos blockToCheck = world.getTopSolidOrLiquidBlock(pos).down();
                hasReef = hasReef || world.getBlockState(blockToCheck).getBlock().equals(ReefModule.REEF_STONE);
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
