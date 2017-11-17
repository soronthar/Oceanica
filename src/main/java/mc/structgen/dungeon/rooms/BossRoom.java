package mc.structgen.dungeon.rooms;

import mc.structgen.dungeon.map.DungeonMap;
import mc.structspawn.manager.StructSpawn;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class BossRoom extends DungeonRoom{
//TODO: render this as a structure
    private final ChunkPos[] bossRoomChunks;

    public BossRoom(DungeonMap dungeonMap, ChunkPos[] bossRoomChunks) {
        super(dungeonMap);
        this.bossRoomChunks =bossRoomChunks;

    }

    @Override
    public void draw(ChunkPos chunkPos, int y, World world) {
        int minX=Integer.MAX_VALUE;
        int minZ=Integer.MAX_VALUE;
        int maxX=Integer.MIN_VALUE;
        int maxZ=Integer.MIN_VALUE;
        for (ChunkPos bossRoomChunk : bossRoomChunks) {
            minX=Math.min(minX,bossRoomChunk.getXStart());
            minZ=Math.min(minZ,bossRoomChunk.getZStart());
            maxX=Math.max(maxX,bossRoomChunk.getXEnd());
            maxZ=Math.max(maxZ,bossRoomChunk.getZEnd());
        }

        int x = (chunkPos.x << 4);
        int z = (chunkPos.z << 4);

        Rotation rotation=Rotation.NONE;
        if (chunkPos.getZStart()==minZ && chunkPos.getXEnd()==maxX) {
            rotation=Rotation.NONE;
        } else if (chunkPos.getZStart()==minZ && chunkPos.getXStart()==minX) {
            rotation=Rotation.COUNTERCLOCKWISE_90;
        } else if (chunkPos.getZEnd()==maxZ && chunkPos.getXEnd()==maxX) {
            rotation=Rotation.CLOCKWISE_90;
        } else if (chunkPos.getZEnd()==maxZ && chunkPos.getXStart()==minX) {
            rotation=Rotation.CLOCKWISE_180;
        }

        //TODO: add entrance
        StructSpawn.generateStructure(world, new BlockPos(x, y, z), "boss_room", rotation);

    }
}
