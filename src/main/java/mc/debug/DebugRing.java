package mc.debug;

import mc.structspawn.StructSpawn;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

/**
 * Created by H440 on 18/10/2017.
 */
public class DebugRing {
    public static  void generateSideOfDebugRing(World world, ChunkPos currentChunk, ChunkPos centerChunk, int radius, int y, IBlockState markerBlock) {
        int xDistance = Math.abs(currentChunk.x - centerChunk.x);
        int zDistance = Math.abs(currentChunk.z - centerChunk.z);

        if (xDistance == radius && zDistance <= radius) {
            //east or west of centerChunk
            int x= (currentChunk.x> centerChunk.x?currentChunk.getXEnd():currentChunk.getXStart());
            int z = currentChunk.getZStart();
            drawZAxis(world, x, y, z, markerBlock);
        }
        if (xDistance <= radius && zDistance == radius) {
            //south or north of centerChunk
            int x=currentChunk.getXStart();
            int z=(currentChunk.z> centerChunk.z?currentChunk.getZEnd():currentChunk.getZStart());
            drawXAxis(world, x, y, z, markerBlock);
        }
    }

    public static  void drawXAxis(World world, int x, int y, int z, IBlockState markerBlock) {
            for (int i = 0; i < 16; i++) {
                world.setBlockState(new BlockPos(x + i, y, z), markerBlock, 2 | 16);
            }
    }

    public static  void drawZAxis(World world, int x, int y, int z, IBlockState markerBlock) {
            for (int i = 0; i < 16; i++) {
                world.setBlockState(new BlockPos(x,  y, z + i), markerBlock, 2 | 16);
            }
    }

    public static  void generateDebugRing(int chunkX, int chunkZ, int y, World world, IBlockState markerBlock) {
        int x = chunkX << 4;
        int z = chunkZ << 4;
        StructSpawn.generateStructure(world, new BlockPos(x,y,z), new ResourceLocation("structgen", "debug/bighollowring"));

    }

    public static void generateSmallDebugRing(int chunkX, int chunkZ, int y, World world, IBlockState markerBlock) {
        int x = (chunkX << 4) +1;
        int z = (chunkZ << 4) +1;

        StructSpawn.generateStructure(world, new BlockPos(x,y,z), new ResourceLocation("structgen", "debug/smallring"));
    }
}
