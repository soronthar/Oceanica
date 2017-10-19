package mc.oceanica.module.reef.world;

import mc.debug.DebugRing;
import mc.oceanica.OceanicaConfig;
import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import mc.oceanica.module.abyss.world.dungeon.rooms.DungeonRoom;
import mc.util.PerlinNoiseGen;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class ExperimentalWorldGen implements IWorldGenerator {


    private PerlinNoiseGen noiseGen;
    private static final int RADIUS = 8;
    private DungeonMap chunkMap;
    public static final int Y_LEVEL = 63;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (noiseGen == null) noiseGen = new PerlinNoiseGen(world.getSeed());


        BlockPos spawnPoint = world.getSpawnPoint();
        ChunkPos referenceChunk=new ChunkPos(spawnPoint);
        if (chunkMap==null || !OceanicaConfig.generateReef)  {
            Vec3i facing = addVec3i(EnumFacing.SOUTH.getDirectionVec(),EnumFacing.EAST.getDirectionVec());
            chunkMap=new DungeonMap(referenceChunk, RADIUS,facing);
            chunkMap.generateMap(RADIUS,random);
            OceanicaConfig.generateReef =true;
        }

        ChunkPos currentChunk=new ChunkPos(chunkX,chunkZ);


        if (chunkMap.contains(currentChunk) && chunkZ!=referenceChunk.z) {
            DebugRing.generateSmallDebugRing(currentChunk.x, currentChunk.z, Y_LEVEL, world, Blocks.WOOL.getStateFromMeta(EnumDyeColor.WHITE.getMetadata()));
        }


        DungeonRoom room=chunkMap.getRoomFor(chunkX,chunkZ);
        if (room!=null) {
            DebugRing.generateSmallDebugRing(chunkX,chunkZ,Y_LEVEL+1,world, Blocks.WOOL.getStateFromMeta(EnumDyeColor.BLUE.getMetadata()) );
        }

        if (chunkX==referenceChunk.x && chunkZ==referenceChunk.z) {
            DebugRing.generateDebugRing(currentChunk.x,currentChunk.z, Y_LEVEL, world, Blocks.COAL_BLOCK);
        }

    }


    private Vec3i addVec3i(Vec3i v, Vec3i u) {
        return new Vec3i(v.getX()+u.getX(),v.getY()+u.getY(),v.getZ()+u.getZ());
    }


}
