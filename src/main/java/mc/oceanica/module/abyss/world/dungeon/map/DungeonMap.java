package mc.oceanica.module.abyss.world.dungeon.map;

import mc.oceanica.module.abyss.world.dungeon.rooms.BossRoom;
import mc.oceanica.module.abyss.world.dungeon.rooms.DungeonRoom;
import mc.oceanica.module.abyss.world.dungeon.rooms.EntryRoom;
import mc.oceanica.module.abyss.world.dungeon.rooms.SimpleRoom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//TODO: have irregular shape
public class DungeonMap {
    private final ChunkPos startChunk;
    private final int radius;
    private final Vec3i facing;
    private final StructureBoundingBox boundingBox;

    private Map<MapCell,DungeonRoom> rooms = new HashMap<>();

    public DungeonMap(ChunkPos startChunk, int radius, Vec3i facing) {
        this.startChunk = startChunk;
        this.radius = radius;
        this.facing = facing;

        int xOffset = radius  * facing.getX();
        int zOffset = radius * facing.getZ();

        boundingBox = StructureBoundingBox.createProper(startChunk.x, 0, startChunk.z, startChunk.x + xOffset, 0, startChunk.z + zOffset);
    }

    public boolean contains(ChunkPos currentChunk) {
        return boundingBox.intersectsWith(currentChunk.x, currentChunk.z, currentChunk.x, currentChunk.z);
    }


    public int chunkToMapZ(int z) {
        return (z - startChunk.z) * facing.getZ();
    }

    public int chunkToMapX(int x) { return ((x - startChunk.x) * facing.getX());
    }

    public int mapToChunkZ(int z) { return startChunk.z + (z * facing.getZ());
    }

    public int mapToChunkX(int x) { return startChunk.x + (x * facing.getX());
    }

    public void generateMap(int radius,Random rand) {

        addRooms(radius, rand);
    }

    private void addRooms(int radius, Random rand) {
        addRoom(new MapCell(0,0), new EntryRoom(this));
        addBossRoom(rand,radius);
        addSimpleRoom(rand,radius);
        addSimpleRoom(rand,radius);
        addSimpleRoom(rand,radius);
        addSimpleRoom(rand,radius);
    }

    private void addSimpleRoom(Random rand, int radius) {
        MapCell mapCell;
        do {
            int cellX = rand.nextInt(radius);
            int cellZ = rand.nextInt(radius );
            mapCell=new MapCell(cellX,cellZ);
        } while (this.rooms.containsKey(mapCell));

        addRoom(mapCell,new SimpleRoom(this));

    }

    private void addBossRoom(Random random, int radius) {

        int middle = radius / 2;
        int cellX = random.nextInt(radius);
        int cellY = random.nextInt(middle) + middle;
        int altX;
        int altZ;

        if (cellX > radius || (random.nextInt() % 2 == 0 && cellX > 0)) {
            altX = cellX - 1;
        } else {
            altX = cellX + 1;
        }

        if (cellY > radius || random.nextInt() % 2 == 0) {
            altZ = cellY - 1;
        } else {
            altZ = cellY + 1;
        }

        MapCell[] bossRoomCells = {new MapCell(cellX, cellY),
                new MapCell(altX, cellY),
                new MapCell(cellX, altZ),
                new MapCell(altX, altZ)
        };

        BossRoom bossRoom=new BossRoom(this,bossRoomCells);

        for (MapCell bossRoomCell : bossRoomCells) {
            addRoom(bossRoomCell, bossRoom);
        }
    }

    public void addRoom(MapCell mapCell, DungeonRoom dungeonRoom) {
        this.rooms.put(mapCell,dungeonRoom);
    }

    public DungeonRoom getRoomFor(int chunkX, int chunkZ) {
        int x = chunkToMapX(chunkX);
        int z = chunkToMapZ(chunkZ);

        return this.rooms.get(new MapCell(x,z));
    }

    public void drawRoomAt(int chunkX, int y, int chunkZ, World world) {
        DungeonRoom room=this.getRoomFor(chunkX,chunkZ);
        if (room!=null) {
            room.draw(chunkX, y, chunkZ, world);
//            DebugRing.generateSmallDebugRing(chunkX,chunkZ,y,world, Blocks.WOOL.getStateFromMeta(EnumDyeColor.BLUE.getMetadata()) );
        }

    }
}
