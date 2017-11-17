package mc.structgen.dungeon.rooms;

import mc.structgen.dungeon.map.DungeonMap;
import mc.structspawn.manager.StructSpawn;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class EntryRoom extends SimpleRoom {

    public EntryRoom(DungeonMap dungeonMap) {
        super(dungeonMap);
    }

    @Override
    public void draw(ChunkPos chunkPos, int y, World world) {
        int x = (chunkPos.x << 4);
        int z = (chunkPos.z << 4);

        StructSpawn.generateStructure(world, new BlockPos(x, y, z), "entry_room", getRotationForExits());

    }


}
