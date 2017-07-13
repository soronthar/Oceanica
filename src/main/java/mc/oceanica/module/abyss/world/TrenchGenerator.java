package mc.oceanica.module.abyss.world;

import mc.oceanica.Oceanica;
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
    private static final int DEEP_SEA_LENGTH = 12;
    private static final int DEEP_SEA_RARITY = 10;
    private static final float DEEP_SEA_WIDTH = 5f;
    private static final float DEEP_SEA_WIDTH_2 = DEEP_SEA_WIDTH * 2F;
    private static final float DEEP_SEA_WIDTH_3 = DEEP_SEA_WIDTH / 4F;
    private static final double DEEP_SEA_DEPTH = 3.25D;
    private static final IBlockState OCEAN_SURFACE = Blocks.GRAVEL.getDefaultState();
    private static final IBlockState OCEAN_FILLER = Blocks.STONE.getDefaultState();

    @Override
    public void generate(World worldIn, int x, int z, ChunkPrimer primer) {
        this.world = worldIn;

        rand.setSeed(worldIn.getSeed());
        long j = rand.nextLong();
        long k = rand.nextLong();

        for (int l = x - DEEP_SEA_LENGTH; l <= x + DEEP_SEA_LENGTH; ++l) {
            for (int i1 = z - DEEP_SEA_LENGTH; i1 <= z + DEEP_SEA_LENGTH; ++i1) {
                long j1 = (long) l * j;
                long k1 = (long) i1 * k;
                rand.setSeed(j1 ^ k1 ^ worldIn.getSeed());
                int rand = this.rand.nextInt(DEEP_SEA_RARITY);

                if (rand == 0) {

                    recursiveGenerate(worldIn, l, i1, x, z, primer);
                }
            }
        }
    }

    @Override
    protected boolean isOceanBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ) {
        return false;
    }

    @Override
    protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int int1, int in2, ChunkPrimer primer) {
        double d1 = findFirstSolidBlock(primer, rand.nextInt(16), rand.nextInt(16));
        double d0 = (double) (chunkX * 16 + rand.nextInt(16));
        double d2 = (double) (chunkZ * 16 + rand.nextInt(16));
        float f = rand.nextFloat() * ((float) Math.PI * DEEP_SEA_WIDTH);
        float f1 = (rand.nextFloat() - DEEP_SEA_WIDTH_3) * DEEP_SEA_WIDTH / DEEP_SEA_WIDTH_2;
        float f2 = (rand.nextFloat() * DEEP_SEA_WIDTH + rand.nextFloat()) * DEEP_SEA_WIDTH;
        addTunnel(rand.nextLong(), int1, in2, primer, d0, d1, d2, f2, f, f1, 0, 0, d2 / DEEP_SEA_DEPTH);
    }


    //TODO: move this to something like "ocean floor blocks"
    private static List SPAWNABLE_BLOCKS = Arrays.asList(Blocks.DIRT, Blocks.GRAVEL, Blocks.SAND, ReefModule.REEF_STONE, ReefModule.CORAL);

    @Override
    protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop) {
        if (y<5 || y > this.world.getSeaLevel()) return;

        Biome biome = this.world.getBiome(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
        if (biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN) {
            IBlockState state = data.getBlockState(x, y, z);
            Block block = state.getBlock();
            if (state.getBlock() == Blocks.STONE || SPAWNABLE_BLOCKS.contains(state.getBlock()) || state.getMaterial().equals(Material.SAND) || (state == Blocks.BEDROCK.getDefaultState())) {
                if (y == 5) {
                    data.setBlockState(x, y, z, OCEAN_SURFACE);
                } else {
                    if (y < this.world.getSeaLevel()) {
                        data.setBlockState(x, y, z, Blocks.WATER.getDefaultState());
                    } else {
                        data.setBlockState(x, y, z, AIR);
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
