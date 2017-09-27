    package mc.oceanica.module.reef.world;

    import mc.oceanica.Oceanica;
    import mc.oceanica.OceanicaConfig;
    import mc.oceanica.OceanicaStats;
    import mc.oceanica.module.reef.ReefModule;
    import mc.oceanica.module.reef.block.BlockCoral;
    import mc.util.PerlinNoiseGen;
    import net.minecraft.block.material.Material;
    import net.minecraft.block.state.IBlockState;
    import net.minecraft.init.Blocks;
    import net.minecraft.item.EnumDyeColor;
    import net.minecraft.util.EnumFacing;
    import net.minecraft.util.math.BlockPos;
    import net.minecraft.world.World;
    import net.minecraft.world.chunk.IChunkProvider;
    import net.minecraft.world.gen.IChunkGenerator;
    import net.minecraftforge.common.BiomeDictionary;
    import net.minecraftforge.fml.common.IWorldGenerator;

    import java.util.Arrays;
    import java.util.List;
    import java.util.Random;

    import static mc.oceanica.module.reef.ReefModule.CORAL;


public class ReefPerlinWorldGenerator implements IWorldGenerator {
    private static final double MIN_LEVEL = 5;
    public static final int REEF_NOISE_LAYER = 0;

    private static List SPAWNABLE_BLOCKS=Arrays.asList(Blocks.DIRT,Blocks.GRAVEL, Blocks.SAND);

    private PerlinNoiseGen noiseGen;
    //TODO: tweak generation
    //TODO: Type Bias according to the depth

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
        if (value < -0.7) {
            //Kelp
        } else if (value<0) {
            //Nothing
        } else {
            Oceanica.logger.info("\t\tGenerating at: " +chunkX + " " + chunkZ + " density " + value);
            OceanicaStats.INSTANCE.addSecondaryDensity(value);
            OceanicaStats.INSTANCE.addSecondaryCount();
            for(int xOffset=0; xOffset<16; xOffset++) {
                for(int zOffset=0; zOffset<16; zOffset++) {
                    if (random.nextDouble()<value ) {
                        addCoral(random, world, x + xOffset, z + zOffset);
                    }
                }
            }
        }
    }

    private void addCoral(Random random, World world, int x, int z) {
        BlockPos blockPos  = new BlockPos(x, 1, z);
        BlockPos topBlock = world.getTopSolidOrLiquidBlock(blockPos).down();
        if (canSpawnInBlock(world,topBlock) && ReefModule.REEF_STONE !=null) {
            world.setBlockState(topBlock, ReefModule.REEF_STONE.getDefaultState(), 2 | 16);
            if (random.nextDouble()<OceanicaConfig.coralDensity ) {
                IBlockState state = CORAL.getDefaultState().withProperty(BlockCoral.CORAL_TYPE, EnumDyeColor.byMetadata(random.nextInt(16)));
                world.setBlockState(topBlock.up(),state, 2 | 16);
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

}
