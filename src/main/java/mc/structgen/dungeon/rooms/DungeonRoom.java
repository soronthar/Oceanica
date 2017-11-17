package mc.structgen.dungeon.rooms;

import mc.structgen.dungeon.map.DungeonMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class DungeonRoom {
    private DungeonMap dungeonMap;
    private Map<EnumFacing, Boolean> exits = new HashMap<>();

    public DungeonRoom(DungeonMap dungeonMap) {
        this.dungeonMap = dungeonMap;
    }

    public DungeonMap getDungeonMap() {
        return dungeonMap;
    }

    public abstract void draw(ChunkPos chunkPos, int y, World world);

    public void addExit(EnumFacing facing) {
        this.exits.put(facing, true);
    }

    public void removeExit(EnumFacing facing) {
        this.exits.remove(facing);
    }

    public int getNumberOfExits() {
        return this.exits.size();
    }

    protected Rotation getRotationForExits() {
        Rotation rotation = Rotation.NONE;

        //Rooms with 4 exits do not require rotation.
        if (this.getNumberOfExits() == 1) {
            //NORTH facing rooms do not need any rotation
            if (hasExits(EnumFacing.EAST)) {
                rotation = Rotation.CLOCKWISE_90;
            } else if (hasExits(EnumFacing.WEST)) {
                rotation = Rotation.COUNTERCLOCKWISE_90;
            } else if (hasExits(EnumFacing.SOUTH)) {
                rotation = Rotation.CLOCKWISE_180;
            }
        } else if (this.getNumberOfExits() == 2) {
            //EnumFacing.NORTH-EnumFacing.SOUTH and EnumFacing.NORTH,EnumFacing.EAST cases do not require rotation.
            if (hasExits(EnumFacing.EAST, EnumFacing.WEST)) {
                rotation = Rotation.CLOCKWISE_90;
            } else if (hasExits(EnumFacing.NORTH, EnumFacing.WEST)) {
                rotation = Rotation.COUNTERCLOCKWISE_90;
            } else if (hasExits(EnumFacing.SOUTH, EnumFacing.EAST)) {
                rotation = Rotation.CLOCKWISE_90;
            } else if (hasExits(EnumFacing.SOUTH, EnumFacing.WEST)) {
                rotation = Rotation.CLOCKWISE_180;
            }
        } else if (this.getNumberOfExits() == 3) {
            //EnumFacing.NORTH-EnumFacing.EAST-EnumFacing.WEST does not require rotation
            if (hasExits(EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST)) {
                rotation = Rotation.CLOCKWISE_180;
            } else if (hasExits(EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.WEST)) {
                rotation = Rotation.COUNTERCLOCKWISE_90;
            } else if (hasExits(EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST)) {
                rotation = Rotation.CLOCKWISE_90;
            }
        }

        return rotation;
    }

    protected boolean hasExits(EnumFacing facing, EnumFacing... facingList) {
        //RANT: Is this really better than iterating over facingList...
        return hasExit(facing) && Arrays.stream(facingList).map(this::hasExit).reduce(true,(aBoolean, aBoolean2) -> aBoolean && aBoolean2);
    }

    private Boolean hasExit(EnumFacing facing) {
        return exits.getOrDefault(facing,false);
    }
}
