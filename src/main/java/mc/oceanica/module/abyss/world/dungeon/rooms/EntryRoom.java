package mc.oceanica.module.abyss.world.dungeon.rooms;

import mc.debug.DebugRing;
import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import mc.structgen.BlockPalette;
import mc.structgen.StructGen;
import mc.structgen.StructureInfo;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by H440 on 22/10/2017.
 */
public class EntryRoom extends SimpleRoom {

    public EntryRoom(DungeonMap dungeonMap) {
        super(dungeonMap);
    }

    @Override
    public void draw(ChunkPos chunkPos, int y, World world) {
        int x = (chunkPos.x << 4);
        int z = (chunkPos.z << 4);

        StructureInfo info=StructGen.loadStructureInfo("/oceanica/dungeon/rooms/entry_room");


        List<EnumFacing> exits = this.getExits();
        EnumFacing facing = exits.get(0);
        Rotation rotation = getRotationForFacing(facing);
        StructGen.generateStructure(world, new BlockPos(x, y, z), info,rotation);

    }

    private Rotation getRotationForFacing(EnumFacing facing) {
        Rotation rotation;
        switch (facing) {
            case EAST:
                rotation=Rotation.CLOCKWISE_90;
                break;
            case WEST:
                rotation=Rotation.COUNTERCLOCKWISE_90;
                break;
            case SOUTH:
                rotation=Rotation.CLOCKWISE_180;
                break;
            case NORTH: //Entry room Exits are always modeled North
            default:
                rotation=Rotation.NONE;
                break;
        }
        return rotation;
    }
}
