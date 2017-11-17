package mc.structgen.dungeon.rooms;

import mc.structgen.dungeon.map.DungeonMap;
import mc.structspawn.manager.StructSpawn;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

//TODO: "collapse" multiple corridor nodes into a single, bigger room
public class Corridor extends DungeonRoom {

    public Corridor(DungeonMap dungeonMap) {
        super(dungeonMap);
    }

    @Override
    public void draw(ChunkPos chunkPos, int y, World world) {
        int x = (chunkPos.x << 4);
        int z = (chunkPos.z << 4);

        String structName="";
        if (this.getNumberOfExits() == 1) {
            structName="corridor_one_exit";
        } else if (this.getNumberOfExits() == 2) {
            if (hasExits(EnumFacing.NORTH, EnumFacing.SOUTH) || hasExits(EnumFacing.EAST, EnumFacing.WEST)) {
                structName="corridor_two_opposite_exits";
            } else {
                structName="corridor_two_adjacent_exits";
            }
        } else if (this.getNumberOfExits() == 3) {
            structName="corridor_three_exits";
        } else if (this.getNumberOfExits() == 4) {
            structName="corridor_four_exits";
        }

        StructSpawn.generateStructure(world, new BlockPos(x, y, z), structName, getRotationForExits());


    }
}
