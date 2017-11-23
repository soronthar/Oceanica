package mc.oceanica.module.abyss.world;

import mc.oceanica.OceanicaConfig;
import mc.oceanica.module.reef.ReefModule;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenRavine;

import java.util.Arrays;
import java.util.List;

public class TrenchGenerator extends MapGenRavine {
    private static final int MIN_DEPTH = 5;

    @Override
    public void generate(World worldIn, int x, int z, ChunkPrimer primer) {
        this.world = worldIn;

        rand.setSeed(worldIn.getSeed());
        long j = rand.nextLong();
        long k = rand.nextLong();

        int xStart = x - OceanicaConfig.abyss.trenches.trenchLength;
        int xEnd = x + OceanicaConfig.abyss.trenches.trenchLength;

        for (int l = xStart; l <= xEnd; ++l) {
            int zStart = z - OceanicaConfig.abyss.trenches.trenchLength;
            int zEnd = z + OceanicaConfig.abyss.trenches.trenchLength;
            for (int i1 = zStart; i1 <= zEnd; ++i1) {
                long j1 = (long) l * j;
                long k1 = (long) i1 * k;
                rand.setSeed(j1 ^ k1 ^ worldIn.getSeed());
                int rand = this.rand.nextInt(OceanicaConfig.abyss.trenches.trenchRarity);

                if (rand == 0) {
                    recursiveGenerate(worldIn, l, i1, x, z, primer);
                }
            }
        }
    }

    //This is a hack to force addTunnel to dig even in the ocean.
    @Override
    protected boolean isOceanBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ) {
        return false;
    }

    @Override
    protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int int1, int in2, ChunkPrimer primer) {
        double d1 = findFirstSolidBlock(primer, rand.nextInt(16), rand.nextInt(16));
        double d0 = (double) (chunkX * 16 + rand.nextInt(16));
        double d2 = (double) (chunkZ * 16 + rand.nextInt(16));
        float trenchWidth = OceanicaConfig.abyss.trenches.trenchWidth;
        float trenchWidthUpper= trenchWidth *2f;
        float trenchWidthLower= trenchWidth /4f;

        float f = rand.nextFloat() * ((float) Math.PI * trenchWidth);
        float f1 = (rand.nextFloat() - trenchWidthLower) * trenchWidth / trenchWidthUpper;
        float f2 = (rand.nextFloat() * trenchWidth + rand.nextFloat()) * trenchWidth;
        addTunnel(rand.nextLong(), int1, in2, primer, d0, d1, d2, f2, f, f1, 0, 0, d2 / OceanicaConfig.abyss.trenches.trenchDepth);
    }


    //TODO: move this to something like "ocean floor blocks"
    private static List SPAWNABLE_BLOCKS = Arrays.asList(Blocks.DIRT, Blocks.GRAVEL, Blocks.SAND, ReefModule.REEF_STONE, ReefModule.CORAL);

    @Override
    protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop) {
        if (y < MIN_DEPTH || y > this.world.getSeaLevel()) return;
        Block oceanFloorBlock = Block.getBlockFromName(OceanicaConfig.abyss.oceanFloorBlock);
        if (oceanFloorBlock==null) return;

        IBlockState oceanFloor = oceanFloorBlock.getDefaultState();

        Biome biome = this.world.getBiome(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
        if (biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN) {
            IBlockState state = data.getBlockState(x, y, z);
            Block block = state.getBlock();
            if (state.getBlock() == Blocks.STONE || SPAWNABLE_BLOCKS.contains(state.getBlock()) || state.getMaterial().equals(Material.SAND) || (state == Blocks.BEDROCK.getDefaultState())) {
                if (y == 5) {
                    data.setBlockState(x, y, z, oceanFloor);
                } else {
                    if (y < this.world.getSeaLevel()) {
                        data.setBlockState(x, y, z, Blocks.WATER.getDefaultState());
                    }
                }
            }
        }
    }

    private int findFirstSolidBlock(ChunkPrimer primer, int x, int z) {
        for (int y = 255; y >= 0; --y) {
            IBlockState iblockstate = primer.getBlockState(x, y, z);
            if (iblockstate != AIR && iblockstate.getBlock() != Blocks.WATER) {
                return y;
            }
        }

        return 0;
    }
}
