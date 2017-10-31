package mc.oceanica.module.abyss.world.dungeon.rooms;

import mc.debug.DebugRing;
import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import mc.structgen.StructGen;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Iterator;

/**
 * Created by H440 on 22/10/2017.
 */
public class Corridor extends DungeonRoom {

    public Corridor(DungeonMap dungeonMap) {
        super(dungeonMap);
    }

    @Override
    public void draw(ChunkPos chunkPos, int y,  World world) {
        int x = (chunkPos.x << 4);
        int z = (chunkPos.z << 4);

        StructGen.generateStructure(world, new BlockPos(x,y,z), new ResourceLocation("structgen", "debug/smallhollowring"));

        for (EnumFacing facing : this.getExits()) {
            int xPos=0;
            int zPos=0;
            switch (facing) {
                case NORTH:
                    xPos=chunkPos.getXStart()+7;
                    zPos=chunkPos.getZStart();
                    break;
                case SOUTH:
                    xPos=chunkPos.getXStart()+7;
                    zPos=chunkPos.getZEnd();
                    break;
                case WEST:
                    xPos=chunkPos.getXStart();
                    zPos=chunkPos.getZStart()+7;
                    break;
                case EAST:
                    xPos=chunkPos.getXEnd();
                    zPos=chunkPos.getZStart()+7;
                    break;
                default:
                    break;
            }

            if (facing!=EnumFacing.UP && facing!=EnumFacing.DOWN) {
                world.setBlockState(new BlockPos(xPos,y,zPos), Blocks.GLOWSTONE.getDefaultState(), 2 | 16);
            }
        }

    }
}
