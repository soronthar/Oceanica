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

//TODO: this "should" be configurable. maybe.
public class ReefPerlinWorldGenerator implements IWorldGenerator {
    private static final int MIN_LEVEL = 5;
    private static final int MAX_LEVEL_OFFSET = 5;
    public static final int REEF_NOISE_LAYER = 0;
    public static final int CORAL_NOISE_LAYER = 2;
    public static final double KELP_NOISE_LAYER = 4;
    public static final double KELP_HEIGHT_LAYER = 5;


    public static final double KELP_CHUNK_THRESHOLD = -0.55;
    public static final double SEED_CHUNK_THRESHOLD = 0.1;

    public static final int REEF_NOISE_SCALE = 2;
    public static final double PRIMARY_REEF_DENSITY_THRESHOLD = 0.2;
    public static final double SECONDARY_REEF_DENSITY_THRESHOLD = 0.5;
    public static final double KELP_DENSITY_THRESHOLD = -0.2;


    private static List SPAWNABLE_BLOCKS=Arrays.asList(Blocks.DIRT,Blocks.GRAVEL, Blocks.SAND);
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

        if (value < KELP_CHUNK_THRESHOLD) {

            for(int xOffset=0; xOffset<16; xOffset++) {
                for(int zOffset=0; zOffset<16; zOffset++) {
                    double density = noiseGen.getValue(x + xOffset, KELP_NOISE_LAYER, z + zOffset);
                    if (density > KELP_DENSITY_THRESHOLD) {
                        BlockPos blockPos  = new BlockPos(x+xOffset, 1, z+zOffset);
                        BlockPos topBlock = world.getTopSolidOrLiquidBlock(blockPos).down();

                        if (canSpawnInBlock(world,topBlock) && ReefModule.KELP !=null) {
                            double heightFactor = noiseGen.getValue(x + xOffset, KELP_HEIGHT_LAYER, z + zOffset);
                            int height= MathHelper.floor((world.getSeaLevel() - 1 - topBlock.getY()) * heightFactor);
                            if (height > 0) {
                                for(int i=1;i<=height;i++) {
                                    world.setBlockState(topBlock.up(i), ReefModule.KELP.getDefaultState(), 2 | 16);
                                }
                            }
//TODO: change the state when the growth mechanism is implemented
//TODO: smooth edges of kelp chunks
                        }
                    }
                }
            }

        } else if (value<= SEED_CHUNK_THRESHOLD){
            if (noiseGen.getValue(chunkX/REEF_NOISE_SCALE , REEF_NOISE_LAYER, (chunkZ + 1) / REEF_NOISE_SCALE) > SEED_CHUNK_THRESHOLD ||
                    noiseGen.getValue(chunkX/REEF_NOISE_SCALE , REEF_NOISE_LAYER, (chunkZ - 1) / REEF_NOISE_SCALE) > SEED_CHUNK_THRESHOLD ||
                    noiseGen.getValue((chunkX+1)/REEF_NOISE_SCALE , REEF_NOISE_LAYER, chunkZ/REEF_NOISE_SCALE ) > SEED_CHUNK_THRESHOLD ||
                    noiseGen.getValue((chunkX+1)/REEF_NOISE_SCALE , REEF_NOISE_LAYER, (chunkZ + 1) / REEF_NOISE_SCALE) > SEED_CHUNK_THRESHOLD ||
                    noiseGen.getValue((chunkX+1)/REEF_NOISE_SCALE , REEF_NOISE_LAYER, (chunkZ - 1) / REEF_NOISE_SCALE) > SEED_CHUNK_THRESHOLD ||
                    noiseGen.getValue((chunkX-1)/REEF_NOISE_SCALE , REEF_NOISE_LAYER, chunkZ/REEF_NOISE_SCALE ) > SEED_CHUNK_THRESHOLD ||
                    noiseGen.getValue((chunkX-1)/REEF_NOISE_SCALE , REEF_NOISE_LAYER, (chunkZ + 1) / REEF_NOISE_SCALE) > SEED_CHUNK_THRESHOLD ||
                    noiseGen.getValue((chunkX-1)/REEF_NOISE_SCALE , REEF_NOISE_LAYER, (chunkZ - 1) / REEF_NOISE_SCALE) > SEED_CHUNK_THRESHOLD) {
                OceanicaStats.INSTANCE.addSecondaryCount();
                for(int xOffset=0; xOffset<16; xOffset++) {
                    for(int zOffset=0; zOffset<16; zOffset++) {
                        double density = noiseGen.getValue(x + xOffset, 2, z + zOffset);
                        if (density > SECONDARY_REEF_DENSITY_THRESHOLD) {
                            addCoral(random, world, x + xOffset, z + zOffset);
                        }
                    }
                }
            }
        } else {
            OceanicaStats.INSTANCE.addPrimarySeed();
            for(int xOffset=0; xOffset<16; xOffset++) {
                for(int zOffset=0; zOffset<16; zOffset++) {
                    double density = noiseGen.getValue(x + xOffset, CORAL_NOISE_LAYER, z + zOffset);
                    if (density > PRIMARY_REEF_DENSITY_THRESHOLD) {
                        addCoral(random, world, x + xOffset, z + zOffset);
                    }
                }
            }
        }
    }

    private double getNoiseForChunk(int chunkX, int chunkZ) {
        return noiseGen.getValue(chunkX/ REEF_NOISE_SCALE, REEF_NOISE_LAYER, chunkZ/ REEF_NOISE_SCALE);
    }

    private void addCoral(Random random, World world, int x, int z) {
        BlockPos blockPos  = new BlockPos(x, 1, z);
        BlockPos topBlock = world.getTopSolidOrLiquidBlock(blockPos).down();
        if (canSpawnInBlock(world,topBlock) && ReefModule.REEF_STONE !=null) {
            world.setBlockState(topBlock, ReefModule.REEF_STONE.getDefaultState(), 2 | 16);
            if (random.nextDouble()< OceanicaConfig.reef.coralDensity ) {
                IBlockState state = CORAL.getDefaultState().withProperty(BlockCoral.CORAL_TYPE, EnumDyeColor.byMetadata(random.nextInt(16)));
                world.setBlockState(topBlock.up(),state, 2 | 16);
            }
        }
    }

    private boolean canSpawnInBlock(World world, BlockPos pos) {
        IBlockState topBlock = world.getBlockState(pos);
        int y = pos.getY();
        boolean isLevel= y >= MIN_LEVEL && y <= world.getSeaLevel()-MAX_LEVEL_OFFSET;
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
