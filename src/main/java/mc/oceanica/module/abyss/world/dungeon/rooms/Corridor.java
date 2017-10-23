package mc.oceanica.module.abyss.world.dungeon.rooms;

import mc.debug.DebugRing;
import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

/**
 * Created by H440 on 22/10/2017.
 */
public class Corridor extends DungeonRoom {

    public Corridor(DungeonMap dungeonMap) {
        super(dungeonMap);
    }

    @Override
    public void draw(int chunkX, int y, int chunkZ, World world) {
        DebugRing.generateSmallDebugRing(chunkX,chunkZ, y, world, Blocks.WOOL.getStateFromMeta(EnumDyeColor.GRAY.getMetadata()));


    }
}
