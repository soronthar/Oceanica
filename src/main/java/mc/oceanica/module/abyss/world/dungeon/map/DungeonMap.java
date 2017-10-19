package mc.oceanica.module.abyss.world.dungeon.map;

import mc.oceanica.module.abyss.world.dungeon.rooms.DungeonRoom;
import mc.oceanica.module.abyss.world.dungeon.rooms.SimpleRoom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//TODO: have irregular shape
public class DungeonMap {
    private final ChunkPos referenceChunk;
    private final int radius;
    private final Vec3i facing;
    private final StructureBoundingBox boundingBox;
    private Map<MapCell,DungeonRoom> rooms = new HashMap<>();

    public DungeonMap(ChunkPos referenceChunk, int radius, Vec3i facing) {
        this.referenceChunk = referenceChunk;
        this.radius = radius;
        this.facing = facing;

        int xOffset = (radius - 1) * facing.getX(); //TODO: Assumes reference chunk is outside, in a corner
        int zOffset = radius * facing.getZ();

        boundingBox = StructureBoundingBox.createProper(referenceChunk.x, 0, referenceChunk.z, referenceChunk.x + xOffset, 0, referenceChunk.z + zOffset);
    }

    public boolean contains(ChunkPos currentChunk) {
        return boundingBox.intersectsWith(currentChunk.x, currentChunk.z, currentChunk.x, currentChunk.z);
    }

    private int getNormalizedZCoord(int z) {
        return (z - referenceChunk.z) * facing.getZ();
    }

    private int getNormalizedXCoord(int x) {
        return ((x - referenceChunk.x) * facing.getX()) + 1;  //TODO: Assumes reference chunk is outside, in a corner
    }

    public void generateMap(int radius,Random rand) {
        Random random = new Random(System.currentTimeMillis());

        int middle = radius / 2;
        int cellX = random.nextInt(radius) + 1;
        int cellY = random.nextInt(middle) + 1 + middle;
        int altX;
        int altZ;

        if (cellX >= radius || (random.nextInt() % 2 == 0 && cellX > 1)) {
            altX = cellX - 1;
        } else {
            altX = cellX + 1;
        }

        if (cellY >= radius || random.nextInt() % 2 == 0) {
            altZ = cellY - 1;
        } else {
            altZ = cellY + 1;
        }


        addRoom(new MapCell(cellX, cellY),new SimpleRoom());
        addRoom(new MapCell(altX, cellY), new SimpleRoom());
        addRoom(new MapCell(cellX, altZ), new SimpleRoom());
        addRoom(new MapCell(altX, altZ), new SimpleRoom());
        System.out.println("cellX = " + cellX);
        System.out.println("cellY = " + cellY);
        System.out.println("altX = " + altX);
        System.out.println("altZ = " + altZ);
    }

    public void addRoom(MapCell mapCell, DungeonRoom dungeonRoom) {
        this.rooms.put(mapCell,dungeonRoom);
    }

    public DungeonRoom getRoomFor(int chunkX, int chunkZ) {
        int x = getNormalizedXCoord(chunkX);
        int z = getNormalizedZCoord(chunkZ);

        return this.rooms.get(new MapCell(x,z));
    }

}
