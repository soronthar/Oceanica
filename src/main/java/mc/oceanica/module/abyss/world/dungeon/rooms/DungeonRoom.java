package mc.oceanica.module.abyss.world.dungeon.rooms;

import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import net.minecraft.world.World;

public abstract class DungeonRoom {
    public DungeonMap dungeonMap;

    public DungeonRoom(DungeonMap dungeonMap) {
        this.dungeonMap=dungeonMap;
    }


    public DungeonMap getDungeonMap() {
        return dungeonMap;
    }


    public abstract void draw(int chunkX, int y, int chunkZ, World world);
}
