package mc.debug;

import mc.oceanica.OceanicaConfig;
import mc.structgen.dungeon.map.DungeonMap;
import mc.util.PerlinNoiseGen;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

import static mc.util.Utils.addVec3i;

public class ExperimentalWorldGen implements IWorldGenerator {


    private PerlinNoiseGen noiseGen;
    private static final int RADIUS = 8;
    private DungeonMap dungeonMap;
    private DungeonMap dungeonMap2;
    public static final int Y_LEVEL = 63;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (noiseGen == null) noiseGen = new PerlinNoiseGen(world.getSeed());

//TODO: Hook up the dungeon generation with the Stucture Generation
        BlockPos spawnPoint = world.getSpawnPoint();
        ChunkPos referenceChunk = new ChunkPos(spawnPoint);
        if (dungeonMap == null || !OceanicaConfig.generateReef) {
            Random rand=new Random();
            Vec3i facing = addVec3i(EnumFacing.NORTH.getDirectionVec(), EnumFacing.WEST.getDirectionVec());
            dungeonMap = new DungeonMap(referenceChunk, RADIUS, facing);
            dungeonMap.generateMap(rand);

            Vec3i facing2 = addVec3i(EnumFacing.SOUTH.getDirectionVec(), EnumFacing.EAST.getDirectionVec());
            dungeonMap2= new DungeonMap(referenceChunk, RADIUS, facing2);
            dungeonMap2.generateMap(rand);
            OceanicaConfig.generateReef = true;
        }

        ChunkPos currentChunk = new ChunkPos(chunkX, chunkZ);

        if (dungeonMap.contains(currentChunk)) {
//            DebugRing.generateSmallDebugRing(currentChunk.x, currentChunk.z, Y_LEVEL, world, Blocks.WOOL.getStateFromMeta(EnumDyeColor.WHITE.getMetadata()));
            dungeonMap.drawRoomAt(currentChunk, Y_LEVEL + 1, world);
        } else if (dungeonMap2.contains(currentChunk)) {
//            DebugRing.generateSmallDebugRing(currentChunk.x, currentChunk.z, Y_LEVEL, world, Blocks.WOOL.getStateFromMeta(EnumDyeColor.WHITE.getMetadata()));
            dungeonMap2.drawRoomAt(currentChunk, Y_LEVEL + 1, world);
        }
    }




}
