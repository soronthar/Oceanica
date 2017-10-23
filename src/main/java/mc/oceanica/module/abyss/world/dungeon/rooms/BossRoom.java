package mc.oceanica.module.abyss.world.dungeon.rooms;

import mc.debug.DebugRing;
import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class BossRoom extends DungeonRoom{

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

        if (chunkPos.getXEnd()==maxX) {
            DebugRing.drawZAxis(world,chunkPos.getXEnd(),y,chunkPos.getZStart(),Blocks.WOOL.getStateFromMeta(EnumDyeColor.YELLOW.getMetadata()));
        } else if (chunkPos.getXStart()==minX) {
            DebugRing.drawZAxis(world,chunkPos.getXStart(),y,chunkPos.getZStart(),Blocks.WOOL.getStateFromMeta(EnumDyeColor.YELLOW.getMetadata()));
        }

        if (chunkPos.getZEnd()==maxZ) {
            DebugRing.drawXAxis(world,chunkPos.getXStart(),y,chunkPos.getZEnd(),Blocks.WOOL.getStateFromMeta(EnumDyeColor.YELLOW.getMetadata()));
        } else if (chunkPos.getZStart()==minZ) {
            DebugRing.drawXAxis(world,chunkPos.getXStart(),y,chunkPos.getZStart(),Blocks.WOOL.getStateFromMeta(EnumDyeColor.YELLOW.getMetadata()));
        }

    }
}
