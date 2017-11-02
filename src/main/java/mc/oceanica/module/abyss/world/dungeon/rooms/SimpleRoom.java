package mc.oceanica.module.abyss.world.dungeon.rooms;

import mc.debug.DebugRing;
import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
//TODO: add doors when generating
public class SimpleRoom extends DungeonRoom {
    public SimpleRoom(DungeonMap dungeonMap) {
        super(dungeonMap);
    }

    @Override
    public void draw(ChunkPos chunkPos, int y, World world) {
        DebugRing.generateDebugRing(chunkPos.x,chunkPos.z,y,world, Blocks.WOOL.getStateFromMeta(EnumDyeColor.BLUE.getMetadata()) );
    }
}
