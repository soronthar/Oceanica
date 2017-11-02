package mc.oceanica.module.abyss.world.dungeon.rooms;

import mc.debug.DebugRing;
import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import mc.structgen.StructGen;
import mc.structgen.StructureInfo;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;


public class Corridor extends DungeonRoom {

    public Corridor(DungeonMap dungeonMap) {
        super(dungeonMap);
    }

    @Override
    public void draw(ChunkPos chunkPos, int y, World world) {
        int x = (chunkPos.x << 4);
        int z = (chunkPos.z << 4);


        if (this.getExits().size() == 1) {
            StructureInfo info = StructGen.loadStructureInfo("oceanica/dungeon/rooms/corridor_one_exit");

            List<EnumFacing> exits = this.getExits();
            EnumFacing facing = exits.get(0);
            Rotation rotation = getRotationForFacing(facing);
            StructGen.generateStructure(world, new BlockPos(x, y, z), info, rotation);
        } else if (this.getExits().size() == 2) {
            ExitFacings exitFacings = getExitFacings();

            StructureInfo info=null;
            Rotation rotation=Rotation.NONE;
            if (exitFacings.north && exitFacings.south ) {
                info = StructGen.loadStructureInfo("oceanica/dungeon/rooms/corridor_two_opposite_exits");
            } else if (exitFacings.east && exitFacings.west) {
                info = StructGen.loadStructureInfo("oceanica/dungeon/rooms/corridor_two_opposite_exits");
                rotation=Rotation.CLOCKWISE_90;
            } else if (exitFacings.north && exitFacings.east) {
                info = StructGen.loadStructureInfo("oceanica/dungeon/rooms/corridor_two_adjacent_exits");
            }else if (exitFacings.north && exitFacings.west) {
                info = StructGen.loadStructureInfo("oceanica/dungeon/rooms/corridor_two_adjacent_exits");
                rotation=Rotation.COUNTERCLOCKWISE_90;
            }else if (exitFacings.south && exitFacings.east) {
                info = StructGen.loadStructureInfo("oceanica/dungeon/rooms/corridor_two_adjacent_exits");
                rotation=Rotation.CLOCKWISE_90;
            }else if (exitFacings.south && exitFacings.west) {
                info = StructGen.loadStructureInfo("oceanica/dungeon/rooms/corridor_two_adjacent_exits");
                rotation=Rotation.CLOCKWISE_180;
            }

            if (info!=null) {
                StructGen.generateStructure(world, new BlockPos(x, y, z), info, rotation);
            } else {
                throw new RuntimeException("Corridor at [" + x + "," + y + "," + z + " has duplicate exits. Won't generate");
            }

        } else if (this.getExits().size() == 3) {
            StructureInfo info = StructGen.loadStructureInfo("oceanica/dungeon/rooms/corridor_three_exits");

            ExitFacings exitFacings = getExitFacings();

            Rotation rotation=Rotation.NONE;
            if (exitFacings.west && exitFacings.east && exitFacings.north) {
                rotation=Rotation.NONE;
            } else if (exitFacings.west && exitFacings.east && exitFacings.south) {
                rotation=Rotation.CLOCKWISE_180;
            } else if (exitFacings.west && exitFacings.north && exitFacings.south) {
                rotation=Rotation.COUNTERCLOCKWISE_90;
            } else if (exitFacings.east && exitFacings.north &&exitFacings. south) {
                rotation=Rotation.CLOCKWISE_90;
            }

            StructGen.generateStructure(world, new BlockPos(x, y, z), info, rotation);

        } else if (this.getExits().size() == 4) {
            StructureInfo info = StructGen.loadStructureInfo("oceanica/dungeon/rooms/corridor_four_exits");
            StructGen.generateStructure(world, new BlockPos(x, y, z), info, Rotation.NONE);
        }

    }

    private ExitFacings getExitFacings() {
        ExitFacings exitFacings=new ExitFacings();
        List<EnumFacing> exits = this.getExits();
        for (EnumFacing facing : exits) {
            switch (facing) {
                case NORTH: exitFacings.north=true;
                    break;
                case SOUTH: exitFacings.south=true;
                    break;
                case WEST: exitFacings.west=true;
                    break;
                case EAST: exitFacings.east=true;
                    break;
                default:
                    break;
            }
        }
        return exitFacings;
    }

    private Rotation getRotationForFacing(EnumFacing facing) {
        Rotation rotation;
        switch (facing) {
            case EAST:
                rotation = Rotation.CLOCKWISE_90;
                break;
            case WEST:
                rotation = Rotation.COUNTERCLOCKWISE_90;
                break;
            case SOUTH:
                rotation = Rotation.CLOCKWISE_180;
                break;
            case NORTH: //Entry room Exits are always modeled North
            default:
                rotation = Rotation.NONE;
                break;
        }
        return rotation;
    }


    private class ExitFacings {
        boolean north = false;
        boolean south = false;
        boolean east = false;
        boolean west = false;
    }
}
