package mc.oceanica.module.abyss.world.dungeon.rooms;

import mc.debug.DebugRing;
import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.World;

public class SimpleRoom extends DungeonRoom {
    public SimpleRoom(DungeonMap dungeonMap) {
        super(dungeonMap);
    }

    @Override
    public void draw(int chunkX, int y, int chunkZ, World world) {
        DebugRing.generateDebugRing(chunkX,chunkZ,y,world, Blocks.WOOL.getStateFromMeta(EnumDyeColor.BLUE.getMetadata()) );
    }
}
