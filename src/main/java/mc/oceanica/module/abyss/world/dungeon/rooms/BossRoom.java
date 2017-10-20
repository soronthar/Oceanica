package mc.oceanica.module.abyss.world.dungeon.rooms;

import mc.debug.DebugRing;
import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import mc.oceanica.module.abyss.world.dungeon.map.MapCell;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class BossRoom extends DungeonRoom{

    private final MapCell[] bossRoomCells;

    public BossRoom(DungeonMap dungeonMap, MapCell[] bossRoomCells) {
        super(dungeonMap);
        this.bossRoomCells=bossRoomCells;
    }


    public MapCell[] getBossRoomCells() {
        return bossRoomCells;
    }

    @Override
    public void draw(int chunkX, int y, int chunkZ, World world) {
        int minX=Integer.MAX_VALUE;
        int minZ=Integer.MAX_VALUE;
        int maxX=Integer.MIN_VALUE;
        int maxZ=Integer.MIN_VALUE;
        for (MapCell bossRoomCell : bossRoomCells) {
            minX=Math.min(minX,bossRoomCell.x);
            minZ=Math.min(minZ,bossRoomCell.z);
            maxX=Math.max(maxX,bossRoomCell.x);
            maxZ=Math.max(maxZ,bossRoomCell.z);
        }

        ChunkPos pos=new ChunkPos(chunkX,chunkZ);
        int mapX = dungeonMap.chunkToMapX(chunkX);
        int mapZ = dungeonMap.chunkToMapZ(chunkZ);

        if (mapX==maxX) {
            DebugRing.drawZAxis(world,pos.getXEnd(),y,pos.getZStart(),Blocks.WOOL.getStateFromMeta(EnumDyeColor.YELLOW.getMetadata()));
        } else if (mapX==minX) {
            DebugRing.drawZAxis(world,pos.getXStart(),y,pos.getZStart(),Blocks.WOOL.getStateFromMeta(EnumDyeColor.YELLOW.getMetadata()));
        }

        if (mapZ==maxZ) {
            DebugRing.drawXAxis(world,pos.getXStart(),y,pos.getZEnd(),Blocks.WOOL.getStateFromMeta(EnumDyeColor.YELLOW.getMetadata()));
        } else if (mapZ==minZ) {
            DebugRing.drawXAxis(world,pos.getXStart(),y,pos.getZStart(),Blocks.WOOL.getStateFromMeta(EnumDyeColor.YELLOW.getMetadata()));
        }

    }
}
