package mc.oceanica.module.reef.world;

import mc.oceanica.OceanicaConfig;
import mc.oceanica.OceanicaStats;
import mc.oceanica.module.reef.ReefModule;
import mc.util.PerlinNoiseGen;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class ExperimentalWorldGen implements IWorldGenerator {

    private static final double MIN_LEVEL = 5;
    public static final int REEF_NOISE_LAYER = 0;
    public static final int CORAL_NOISE_LAYER = 2;
    public static final double PRIMARY_CORAL_THRESHOLD = 0.2;
    public static final double SECONDARY_REEF_THRESHOLD = 0.5;


    private PerlinNoiseGen noiseGen;
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        OceanicaStats.INSTANCE.addChunkProcessed();
        if (OceanicaConfig.generateReef) {
            if (noiseGen == null) noiseGen = new PerlinNoiseGen(world.getSeed());
            generateReefInChunk(random, world, chunkX, chunkZ);
        }
    }

    private void generateReefInChunk(Random random, World world, int chunkX, int chunkZ) {

        int x = chunkX << 4;
        int z = chunkZ << 4;

        int FEATURE_SIZE=2;

        double value = noiseGen.getValue(chunkX/FEATURE_SIZE , REEF_NOISE_LAYER, chunkZ/FEATURE_SIZE );
        if (value < -0.65) {
            int y=4;
            world.setBlockState(new BlockPos(x,y,z), Blocks.DIRT.getDefaultState(), 2 | 16);
            for (int i=1;i<16;i++) {
                IBlockState markerBlock = Blocks.LEAVES.getDefaultState();
                world.setBlockState(new BlockPos(x+i,y,z), markerBlock, 2 | 16);
                world.setBlockState(new BlockPos(x,y,z+i), markerBlock, 2 | 16);
                world.setBlockState(new BlockPos(x+i,y,z+15), markerBlock, 2 | 16);
                world.setBlockState(new BlockPos(x+15,y,z+i), markerBlock, 2 | 16);
            }
        } else if (value<=0.1) {
            if (noiseGen.getValue(chunkX/FEATURE_SIZE , REEF_NOISE_LAYER, (chunkZ + 1) / FEATURE_SIZE) > 0.1 ||
                    noiseGen.getValue(chunkX/FEATURE_SIZE , REEF_NOISE_LAYER, (chunkZ - 1) / FEATURE_SIZE) > 0.1 ||
                    noiseGen.getValue((chunkX+1)/FEATURE_SIZE , REEF_NOISE_LAYER, chunkZ/FEATURE_SIZE ) > 0.1 ||
                    noiseGen.getValue((chunkX+1)/FEATURE_SIZE , REEF_NOISE_LAYER, (chunkZ + 1) / FEATURE_SIZE) > 0.1 ||
                    noiseGen.getValue((chunkX+1)/FEATURE_SIZE , REEF_NOISE_LAYER, (chunkZ - 1) / FEATURE_SIZE) > 0.1 ||
                    noiseGen.getValue((chunkX-1)/FEATURE_SIZE , REEF_NOISE_LAYER, chunkZ/FEATURE_SIZE ) > 0.1 ||
                    noiseGen.getValue((chunkX-1)/FEATURE_SIZE , REEF_NOISE_LAYER, (chunkZ + 1) / FEATURE_SIZE) > 0.1 ||
                    noiseGen.getValue((chunkX-1)/FEATURE_SIZE , REEF_NOISE_LAYER, (chunkZ - 1) / FEATURE_SIZE) > 0.1) {
                for(int xOffset=0; xOffset<16; xOffset++) {
                    for(int zOffset=0; zOffset<16; zOffset++) {
                        double density = noiseGen.getValue(x + xOffset, 2, z + zOffset);
                        if (density > SECONDARY_REEF_THRESHOLD) {
                            addCoral(random, world, x + xOffset, z + zOffset);
                        }
                    }
                }
            }
        } else {
            for(int xOffset=0; xOffset<16; xOffset++) {
                for(int zOffset=0; zOffset<16; zOffset++) {
                    double density = noiseGen.getValue(x + xOffset, CORAL_NOISE_LAYER, z + zOffset);
                    if (density > PRIMARY_CORAL_THRESHOLD) {
                        addCoral(random, world, x + xOffset, z + zOffset);
                    }
                }
            }
        }
    }

    private void addCoral(Random random, World world, int x, int z) {
        BlockPos topBlock = new BlockPos(x, 4, z);;
        if (ReefModule.REEF_STONE !=null) {
            world.setBlockState(topBlock, ReefModule.REEF_STONE.getDefaultState(), 2 | 16);
        }
    }


}
