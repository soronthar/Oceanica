package mc.oceanica.module.abyss.world.dungeon.rooms;

import mc.debug.DebugRing;
import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class BossRoom extends DungeonRoom{

    private final ChunkPos[] chunks;

    public BossRoom(DungeonMap dungeonMap, ChunkPos[] bossRoomChunks) {
        super(dungeonMap);
        this.chunks=bossRoomChunks;

    }

    @Override
    public void draw(int chunkX, int y, int chunkZ, World world) {
        int minX=Integer.MAX_VALUE;
        int minZ=Integer.MAX_VALUE;
        int maxX=Integer.MIN_VALUE;
        int maxZ=Integer.MIN_VALUE;
        for (ChunkPos chunkPos : chunks) {
            minX=Math.min(minX,chunkPos.getXStart());
            minZ=Math.min(minZ,chunkPos.getZStart());
            maxX=Math.max(maxX,chunkPos.getXEnd());
            maxZ=Math.max(maxZ,chunkPos.getZEnd());
        }
        ChunkPos pos=new ChunkPos(chunkX,chunkZ);

        if (pos.getXEnd()==maxX) {
            DebugRing.drawZAxis(world,pos.getXEnd(),y,pos.getZStart(),Blocks.WOOL.getStateFromMeta(EnumDyeColor.YELLOW.getMetadata()));
        } else if (pos.getXStart()==minX) {
            DebugRing.drawZAxis(world,pos.getXStart(),y,pos.getZStart(),Blocks.WOOL.getStateFromMeta(EnumDyeColor.YELLOW.getMetadata()));
        }

        if (pos.getZEnd()==maxZ) {
            DebugRing.drawXAxis(world,pos.getXStart(),y,pos.getZEnd(),Blocks.WOOL.getStateFromMeta(EnumDyeColor.YELLOW.getMetadata()));
        } else if (pos.getZStart()==minZ) {
            DebugRing.drawXAxis(world,pos.getXStart(),y,pos.getZStart(),Blocks.WOOL.getStateFromMeta(EnumDyeColor.YELLOW.getMetadata()));
        }

    }
}
