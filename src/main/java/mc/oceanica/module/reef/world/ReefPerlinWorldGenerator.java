    package mc.oceanica.module.reef.world;

import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static mc.oceanica.module.reef.ReefModule.CORAL;


public class ReefPerlinWorldGenerator implements IWorldGenerator {
    private static final double MIN_LEVEL = 5;

    private static List SPAWNABLE_BLOCKS=Arrays.asList(Blocks.DIRT,Blocks.GRAVEL, Blocks.SAND);
    private Object2BooleanArrayMap<ChunkCoord> coralChunks=new Object2BooleanArrayMap<>();

    private NoiseGeneratorPerlin noiseGen;
    //TODO: tweak generation
    //TODO: Density Bias according to the depth
    //TODO: Type Bias according to the depth

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        OceanicaStats.INSTANCE.addChunkProcessed();
        if (OceanicaConfig.generateReef) {
            if (noiseGen==null) noiseGen=new NoiseGeneratorPerlin(new Random(world.getSeed()),1);

            if (isSeedChunk(chunkX, chunkZ)) {
                OceanicaStats.INSTANCE.addPrimarySeed();

                if (OceanicaConfig.isDebug) {
                    generateDebugRing(chunkX, chunkZ, world);
                }

                generateReefInChunk(random, world, chunkX, chunkZ, OceanicaConfig.reefStoneDensity);
            } else if (OceanicaConfig.enableSecondarySeedChunk && isSecondarySeedChunk(chunkX,chunkZ)) {
                double reducedDensity = OceanicaConfig.reefStoneDensity - OceanicaConfig.densityDecrease; //move this to the config.
                generateReefInChunk(random, world, chunkX, chunkZ,reducedDensity);
            } else if (isSecondaryChunk(chunkX, chunkZ)) {
                double decreaseFactor= OceanicaConfig.baseDecreaseFactor +random.nextDouble();
                double density=decreaseFactor * OceanicaConfig.densityDecrease;
                double secondaryDensity = OceanicaConfig.reefStoneDensity - density;

                if (secondaryDensity>0) {
                    OceanicaStats.INSTANCE.addSecondaryCount();
                    OceanicaStats.INSTANCE.addSecondaryDensity(secondaryDensity);
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
        boolean isLevel= y >= MIN_LEVEL && y <= world.getSeaLevel()-3;
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

// Cache isSeedChunk. Play with locality
    private boolean isSeedChunk(int chunkX, int chunkZ) {
        ChunkCoord chunkCoord=new ChunkCoord(chunkX,chunkZ);
        if (coralChunks.containsKey(chunkCoord)) {
            return coralChunks.getBoolean(chunkCoord);
        } else {
            boolean result = Math.abs(noiseGen.getValue(chunkCoord.getLeft(), chunkCoord.getRight())) < OceanicaConfig.seedChunkFrequency;
            coralChunks.put(chunkCoord,result);
            return result;
        }
    }

    private boolean isSecondarySeedChunk(int chunkX, int chunkZ) {
        ChunkCoord chunkCoord=new ChunkCoord(chunkX,chunkZ);
        if (coralChunks.containsKey(chunkCoord)) {
            return coralChunks.getBoolean(chunkCoord);
        } else {
            boolean result=isSeedChunk(chunkX +1 , chunkZ) ||
                    isSeedChunk(chunkX +1 , chunkZ+1) ||
                    isSeedChunk(chunkX +1 , chunkZ-1) ||
                    isSeedChunk(chunkX -1 , chunkZ) ||
                    isSeedChunk(chunkX -1 , chunkZ+1) ||
                    isSeedChunk(chunkX -1 , chunkZ-1) ||
                    isSeedChunk(chunkX, chunkZ+1) ||
                    isSeedChunk(chunkX, chunkZ-1);
            coralChunks.put(chunkCoord,result);
            return result;
        }
    }

    private boolean isSecondaryChunk(int chunkX, int chunkZ) {
        ChunkCoord chunkCoord=new ChunkCoord(chunkX,chunkZ);
        if (coralChunks.containsKey(chunkCoord)) {
            return coralChunks.getBoolean(chunkCoord);
        } else {
            boolean result=(IsCoralChunk(chunkX +1, chunkZ) ||
                    IsCoralChunk(chunkX -1, chunkZ) ||
                    IsCoralChunk(chunkX , chunkZ-1) ||
                    IsCoralChunk(chunkX , chunkZ+1)) && Math.abs(noiseGen.getValue(chunkX,chunkZ)) < 0.2f;
            coralChunks.put(chunkCoord,result);
            return result;
        }
    }

    private boolean IsCoralChunk(int chunkX, int chunkZ) {
        ChunkCoord chunkCoord=new ChunkCoord(chunkX,chunkZ);
        if (coralChunks.containsKey(chunkCoord)) {
            return coralChunks.getBoolean(chunkCoord);
        } else {
            boolean result=isSeedChunk(chunkX , chunkZ) || isSecondarySeedChunk(chunkX, chunkZ);;
            coralChunks.put(chunkCoord,result);
            return result;
        }
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

    private static class ChunkCoord extends MutablePair<Integer,Integer> {
        public ChunkCoord(Integer chunkX,Integer chunkZ) {
            super(chunkX, chunkZ);
        }
    }
}
