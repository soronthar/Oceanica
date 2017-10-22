package mc.oceanica.module.reef.world;

import mc.debug.DebugRing;
import mc.oceanica.OceanicaConfig;
import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
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
    private static final int RADIUS = 5;
    private DungeonMap dungeonMap;
    public static final int Y_LEVEL = 63;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (noiseGen == null) noiseGen = new PerlinNoiseGen(world.getSeed());


        BlockPos spawnPoint = world.getSpawnPoint();
        ChunkPos referenceChunk = new ChunkPos(spawnPoint);
        if (dungeonMap == null || !OceanicaConfig.generateReef) {
            Vec3i facing = addVec3i(EnumFacing.SOUTH.getDirectionVec(), EnumFacing.WEST.getDirectionVec());
            dungeonMap = new DungeonMap(referenceChunk, RADIUS, facing);

            dungeonMap.generateMap(RADIUS, random);

            OceanicaConfig.generateReef = true;
        }

        ChunkPos currentChunk = new ChunkPos(chunkX, chunkZ);

        if (dungeonMap.contains(currentChunk)) {
            DebugRing.generateSmallDebugRing(currentChunk.x, currentChunk.z, Y_LEVEL, world, Blocks.WOOL.getStateFromMeta(EnumDyeColor.WHITE.getMetadata()));
            dungeonMap.drawRoomAt(chunkX, Y_LEVEL + 1, chunkZ, world);
        }
    }


    private Vec3i addVec3i(Vec3i v, Vec3i u) {
        return new Vec3i(v.getX() + u.getX(), v.getY() + u.getY(), v.getZ() + u.getZ());
    }


}
