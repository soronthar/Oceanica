package mc.oceanica.module.abyss.world.dungeon.rooms;

import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class DungeonRoom {
    private DungeonMap dungeonMap;
    private List<EnumFacing> exits=new ArrayList<>();

    public DungeonRoom(DungeonMap dungeonMap) {
        this.dungeonMap=dungeonMap;
    }


    public DungeonMap getDungeonMap() {
        return dungeonMap;
    }


    public abstract void draw(int chunkX, int y, int chunkZ, World world);

    public void addExit(EnumFacing facing) {
        this.exits.add(facing);
    }
}
