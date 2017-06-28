package mc.oceanica.module.reef.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;


public class LeavesMarkWorldGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
        int x = chunkX << 4;
        int z = chunkZ << 4;
        BlockPos topSolidOrLiquidBlock = getTopSolidOrLiquidBlock(world, chunk, x, z);
        world.setBlockState(topSolidOrLiquidBlock.up(2), Blocks.EMERALD_BLOCK.getDefaultState(), 2 | 16);
    }

    private boolean conditionsAreGiven(World world, int x, int z, BlockPos topSolidOrLiquidBlock) {
        return x%10==0 && z%15==0 && world.getBlockState(topSolidOrLiquidBlock).getBlock().equals(Blocks.SANDSTONE);
    }

    public static BlockPos getTopSolidOrLiquidBlock(World world, Chunk chunk, int x, int z) {
        BlockPos blockpos;
        BlockPos blockpos1;

        for (blockpos = new BlockPos(x, chunk.getTopFilledSegment() + 16, z); blockpos.getY() >= 0; blockpos = blockpos1) {
            blockpos1 = blockpos.down();
            IBlockState state = chunk.getBlockState(blockpos1);
            if (state.getMaterial().isSolid() && !state.getMaterial().isLiquid() && !state.getBlock().isLeaves(state, world, blockpos1) && !state.getBlock().isFoliage(world, blockpos1)) {
                break;
            }
        }
        return blockpos.down();
    }
}
