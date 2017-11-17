package mc.structgen.dungeon.map;

import mc.structgen.dungeon.rooms.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.*;

//TODO: have irregular shape
public class DungeonMap {
    private final ChunkPos startChunk;
    private final int radius;
    private final Vec3i facing;
    private final StructureBoundingBox boundingBox;

    private Map<ChunkPos,DungeonRoom> rooms = new HashMap<>();

    public DungeonMap(ChunkPos startChunk, int radius, Vec3i facing) {
        this.startChunk = startChunk;
        this.radius = radius;
        this.facing = facing;

        int xOffset = radius  * facing.getX();
        int zOffset = radius * facing.getZ();

        boundingBox = StructureBoundingBox.createProper(startChunk.x, 0, startChunk.z, startChunk.x + xOffset, 0, startChunk.z + zOffset);
    }

    public int getRadius() {
        return radius;
    }

    public Vec3i getFacing() {
        return facing;
    }

    public ChunkPos getStartChunk() {
        return startChunk;
    }

    public boolean contains(ChunkPos currentChunk) {
        return boundingBox.intersectsWith(currentChunk.x, currentChunk.z, currentChunk.x, currentChunk.z);
    }

    public void generateMap(Random rand) {
        addRooms(rand);
        generateCorridors(rand);
    }

    private void generateCorridors(Random rand) {
        carvePassageFrom(startChunk.x,startChunk.z,rand);
    }

    private void carvePassageFrom(int x, int z, Random rand) {
        DungeonRoom room=rooms.get(new ChunkPos(x,z));
        if (room==null) return;

        List<EnumFacing> facingList = Arrays.asList(EnumFacing.values());
        Collections.shuffle(facingList,rand);
        for(EnumFacing facing:facingList) {
            Vec3i vec = facing.getDirectionVec();
            int newX=x+vec.getX();
            int newZ=z+vec.getZ();
            if (boundingBox.intersectsWith(newX,newZ,newX,newZ) && !this.rooms.containsKey(new ChunkPos(newX,newZ))) {
                room.addExit(facing);
                Corridor corridor = new Corridor(this);
                corridor.addExit(facing.getOpposite());
                addRoom(new ChunkPos(newX,newZ),corridor);
                carvePassageFrom(newX,newZ,rand);
            }
        }


    }

    private void addRooms(Random rand) {
        addRoom(startChunk, new EntryRoom(this));
        addBossRoom(rand, this.radius);
        addSimpleRoom(rand,this.radius);
        addSimpleRoom(rand,this.radius);
        addSimpleRoom(rand,this.radius);
        addSimpleRoom(rand,this.radius);
    }

    private void addSimpleRoom(Random rand, int radius) {
        ChunkPos chunkPos;

        do {
            int cellX = rand.nextInt(boundingBox.getXSize())+boundingBox.minX;
            int cellZ = rand.nextInt(boundingBox.getZSize())+boundingBox.minZ;
            chunkPos=new ChunkPos(cellX,cellZ);
        } while (this.rooms.containsKey(chunkPos));

        addRoom(chunkPos,new SimpleRoom(this));

    }

    private void addBossRoom(Random random, int radius) {

        int middleX = boundingBox.getXSize() / 2;
        int middleZ = boundingBox.getZSize() / 2;
        int cellX = ((random.nextInt(middleX) + middleX) * facing.getX()) + startChunk.x;
        int cellZ = ((random.nextInt(middleZ) + middleZ) * facing.getZ()) + startChunk.z;
        int altX;
        int altZ;


        altX = calculateAltCoord(cellX, facing.getX(), boundingBox.maxX, boundingBox.minX, random);
        altZ = calculateAltCoord(cellZ, facing.getZ(), boundingBox.maxZ, boundingBox.minZ, random);

        ChunkPos[] bossRoomCells = {new ChunkPos(cellX, cellZ),
                new ChunkPos(altX, cellZ),
                new ChunkPos(cellX, altZ),
                new ChunkPos(altX, altZ)
        };

        BossRoom bossRoom=new BossRoom(this,bossRoomCells);

        for (ChunkPos bossRoomChunk : bossRoomCells) {
            addRoom(bossRoomChunk, bossRoom);
        }
    }

    private int calculateAltCoord(int coord, int facing, int max, int min, Random random) {
        int altX;

        if (coord+ facing > max || coord+ facing < min) {
            //if going with the facing take us outside the bound, go against it.
            altX = coord - facing;
        } else if (coord- facing > max || coord- facing < min) {
            //if going against the facing take us outside the bound, go with it.
            altX = coord + facing;
        } else if (random.nextInt() % 2 == 0) { //randombly choose which way to go.
            altX = coord + facing;
        } else {
            altX = coord - facing;
        }
        return altX;
    }

    public void addRoom(ChunkPos chunkPos, DungeonRoom dungeonRoom) {
        this.rooms.put(chunkPos,dungeonRoom);
    }

    public DungeonRoom getRoomFor(int chunkX, int chunkZ) {
        return getRoomFor(new ChunkPos(chunkX,chunkZ));
    }

    private DungeonRoom getRoomFor(ChunkPos chunkPos) {
        return this.rooms.get(chunkPos);
    }


    public void drawRoomAt(ChunkPos chunkPos, int y, World world) {
        DungeonRoom room=this.getRoomFor(chunkPos);
        if (room!=null) {
            room.draw(chunkPos, y, world);
//            DebugRing.generateSmallDebugRing(chunkX,chunkZ,y,world, Blocks.WOOL.getStateFromMeta(EnumDyeColor.BLUE.getMetadata()) );
        }

    }


}
