package mc.oceanica.module.abyss.world.dungeon.rooms;

import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
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

        Rotation rotation = getRotationForExits();
        if (this.getNumberOfExits() == 1) {
//            info = StructSpawn.loadStructureInfo("oceanica/dungeon/rooms/corridor_one_exit");
        } else if (this.getNumberOfExits() == 2) {
            if (hasExits(EnumFacing.NORTH, EnumFacing.SOUTH) ||
                    hasExits(EnumFacing.EAST, EnumFacing.WEST)) {
//                info = StructSpawn.loadStructureInfo("oceanica/dungeon/rooms/corridor_two_opposite_exits");
            } else {
//                info = StructSpawn.loadStructureInfo("oceanica/dungeon/rooms/corridor_two_adjacent_exits");
            }
        } else if (this.getNumberOfExits() == 3) {
//            info = StructSpawn.loadStructureInfo("oceanica/dungeon/rooms/corridor_three_exits");
        } else if (this.getNumberOfExits() == 4) {
//            info = StructSpawn.loadStructureInfo("oceanica/dungeon/rooms/corridor_four_exits");
        }

//        StructSpawn.generateStructure(world, new BlockPos(x, y, z), info, rotation);


    }
}
