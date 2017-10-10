package mc.oceanica.module.reef.world;

import mc.util.PerlinNoiseGen;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class ExperimentalWorldGen implements IWorldGenerator {


    private PerlinNoiseGen noiseGen;
    private static final int RADIUS = 4;
    public static final int EAST = 1;
    public static final int WEST = 2;
    public static final int NORTH = 4;
    public static final int SOUTH = 8;

    //    private PerlinNoiseGen noiseGen;
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (noiseGen == null) noiseGen = new PerlinNoiseGen(world.getSeed());

        BlockPos spawnPoint = world.getSpawnPoint();
        int y = 64;

        ChunkPos referenceChunk=new ChunkPos(spawnPoint);
        ChunkPos currentChunk=new ChunkPos(chunkX,chunkZ);

        int xOffset=(RADIUS-1);
        int zOffset=RADIUS;

        int maxX = referenceChunk.x + xOffset  ;
        int maxZ = referenceChunk.z + zOffset  ;
        StructureBoundingBox boundingBox=new StructureBoundingBox(referenceChunk.x,0,referenceChunk.z,referenceChunk.x,0,referenceChunk.z);
        boundingBox.expandTo(new StructureBoundingBox(maxX,0,maxZ,maxX,0,maxZ));


        if (chunkX==referenceChunk.x && chunkZ==referenceChunk.z) {
            generateDebugRing(currentChunk.x,currentChunk.z, y, world, Blocks.COAL_BLOCK);
            return;
        }

        if (boundingBox.intersectsWith(currentChunk.x,currentChunk.z,currentChunk.x,currentChunk.z) && chunkZ!=referenceChunk.z) {
            generateSmallDebugRing(currentChunk.x,currentChunk.z, y, world, Blocks.WOOL.getStateFromMeta(EnumDyeColor.WHITE.getMetadata()));
            int[][] maze=new int[4][4];
            for (int j=0;j<4;j++) {
                int runStart=0;
                boolean lastCarveEast=false;
                for(int i=0;i<4;i++) {
                    if (j>0 && random.nextDouble()<0.5) {
                        if (lastCarveEast) {
                            maze[i][j]|= NORTH |WEST;
                        } else {
                            maze[i][j]|= NORTH;
                        }
                        maze[i][j-1]|=SOUTH;

                        lastCarveEast=false;
                    } else {
                        if (lastCarveEast) {
                            maze[i][j]|= EAST | WEST;
                        } else {
                            maze[i][j]|= EAST;
                            lastCarveEast=true;
                        }
                    }
                }
            }
            maze[2][0]|=NORTH;
            maze[2][3]|=SOUTH;


            IBlockState wall = Blocks.WOOL.getStateFromMeta(random.nextInt(16));

            for(int z=currentChunk.getZStart();z<currentChunk.getZEnd();z+=4) {
                for(int x=currentChunk.getXStart();x<currentChunk.getXEnd();x+=4) {
                    drawRing(x,y+1,z,4,wall,world,maze[(x-currentChunk.getXStart())/4][(z-currentChunk.getZStart())/4]);

                }
            }

        }
    }

    private void drawRing(int x, int y, int z, int size, IBlockState wall, World world,int carving) {
        for(int j=0;j<size;j++) {
            if (j==0 ) {
                for(int i=0;i<size;i++) {
                    if ((carving&NORTH)!=NORTH) {
                        world.setBlockState(new BlockPos(x+i,y,z+j), wall, 2 | 16);
                    } else if (i==0 || i==size-1){
                        world.setBlockState(new BlockPos(x+i,y,z+j), wall, 2 | 16);
                    }
                }
            } else if (j==size-1) {
                for(int i=0;i<size;i++) {
                    if ((carving&SOUTH)!=SOUTH) {
                        world.setBlockState(new BlockPos(x+i,y,z+j), wall, 2 | 16);
                    } else if (i==0 || i==size-1){
                        world.setBlockState(new BlockPos(x+i,y,z+j), wall, 2 | 16);
                    }
                }
            } else {
                if ((carving&WEST)!=WEST) {
                    world.setBlockState(new BlockPos(x, y, z + j), wall, 2 | 16);
                }
                if ((carving&EAST)!=EAST) {
                    world.setBlockState(new BlockPos(x + size - 1, y, z + j), wall, 2 | 16);
                }
            }
        }
    }

    private void generateSideOfDebugRing(World world, ChunkPos currentChunk, ChunkPos centerChunk, int radius, int y, IBlockState markerBlock) {
        int xDistance = Math.abs(currentChunk.x - centerChunk.x);
        int zDistance = Math.abs(currentChunk.z - centerChunk.z);

        if (xDistance == radius && zDistance <= radius) {
            //east or west of spawnchunk
            int x= (currentChunk.x> centerChunk.x?currentChunk.getXEnd():currentChunk.getXStart());
            int z = currentChunk.getZStart();
            drawZAxis(world, x, y, z, markerBlock);
        }
        if (xDistance <= radius && zDistance == radius) {
            //south or north of spawnchunk
            int x=currentChunk.getXStart();
            int z=(currentChunk.z> centerChunk.z?currentChunk.getZEnd():currentChunk.getZStart());
            drawXAxis(world, x, y, z, markerBlock);
        }
    }

    private void drawXAxis(World world, int x, int y, int z, IBlockState markerBlock) {
        for (int i = 0; i< 16; i++) {
            world.setBlockState(new BlockPos(x+i, y,z), markerBlock, 2 | 16);
        }
    }

    private void drawZAxis(World world, int x, int y, int z, IBlockState markerBlock) {
        for (int i = 0; i< 16; i++) {
            world.setBlockState(new BlockPos(x, y,z+i), markerBlock, 2 | 16);
        }
    }

    private void generateDebugRing(int chunkX, int chunkZ, int y, World world, Block block) {
        int x = chunkX << 4;
        int z = chunkZ << 4;
        IBlockState markerBlock = block.getDefaultState();
        IBlockState state = Blocks.BONE_BLOCK.getDefaultState();
        world.setBlockState(new BlockPos(x,y,z), state, 2 | 16);

        for (int i=1;i<16;i++) {
            world.setBlockState(new BlockPos(x+i,y,z), state, 2 | 16);
            world.setBlockState(new BlockPos(x,y,z+i), markerBlock, 2 | 16);
            world.setBlockState(new BlockPos(x+i,y,z+15), markerBlock, 2 | 16);
            world.setBlockState(new BlockPos(x+15,y,z+i), markerBlock, 2 | 16);
        }
    }

    private void generateSmallDebugRing(int chunkX, int chunkZ, int y, World world,  IBlockState markerBlock) {
        int x = (chunkX << 4) +1;
        int z = (chunkZ << 4) +1;
        world.setBlockState(new BlockPos(x,y,z), Blocks.DIAMOND_BLOCK.getDefaultState(), 2 | 16);

        for (int i=1;i<14;i++) {
            world.setBlockState(new BlockPos(x+i,y,z), markerBlock, 2 | 16);
            world.setBlockState(new BlockPos(x,y,z+i), markerBlock, 2 | 16);
            world.setBlockState(new BlockPos(x+i,y,z+13), markerBlock, 2 | 16);
            world.setBlockState(new BlockPos(x+13,y,z+i), markerBlock, 2 | 16);
        }
    }
}
