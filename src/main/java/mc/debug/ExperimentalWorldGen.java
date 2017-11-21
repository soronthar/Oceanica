package mc.debug;

import mc.oceanica.OceanicaConfig;
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

public class ExperimentalWorldGen implements IWorldGenerator {


    private PerlinNoiseGen noiseGen;
    private static final int RADIUS = 8;
    public static final int Y_LEVEL = 63;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (noiseGen == null) noiseGen = new PerlinNoiseGen(world.getSeed());

    }




}
