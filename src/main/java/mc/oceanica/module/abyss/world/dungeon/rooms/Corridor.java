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

/**
 * Created by H440 on 22/10/2017.
 */
public class Corridor extends DungeonRoom {

    public Corridor(DungeonMap dungeonMap) {
        super(dungeonMap);
    }

    @Override
    public void draw(ChunkPos chunkPos, int y, World world) {
        int x = (chunkPos.x << 4);
        int z = (chunkPos.z << 4);


        if (this.getExits().size() == 1) {
            StructureInfo info = StructGen.loadStructureInfo("/oceanica/dungeon/rooms/corridor_one_exit");

            List<EnumFacing> exits = this.getExits();
            EnumFacing facing = exits.get(0);
            Rotation rotation = getRotationForFacing(facing);
            StructGen.generateStructure(world, new BlockPos(x, y, z), info, rotation);
        } else if (this.getExits().size() == 3) {
            StructureInfo info = StructGen.loadStructureInfo("/oceanica/dungeon/rooms/corridor_three_exits");

            boolean north = false;
            boolean south = false;
            boolean east = false;
            boolean west = false;
            List<EnumFacing> exits = this.getExits();
            for (EnumFacing facing : exits) {
                switch (facing) {
                    case NORTH: north=true;
                        break;
                    case SOUTH: south=true;
                        break;
                    case WEST: west=true;
                        break;
                    case EAST: east=true;
                        break;
                    default:
                        break;
                }
            }

            Rotation rotation=Rotation.NONE;
            if (west && east && north) {
                rotation=Rotation.NONE;
            } else if (west && east && south) {
                rotation=Rotation.CLOCKWISE_180;
            } else if (west && north && south) {
                rotation=Rotation.COUNTERCLOCKWISE_90;
            } else if (east && north && south) {
                rotation=Rotation.CLOCKWISE_90;
            }

            StructGen.generateStructure(world, new BlockPos(x, y, z), info, rotation);

        } else {
            StructGen.generateStructure(world, new BlockPos(x, y, z), new ResourceLocation("structgen", "debug/smallhollowring"));

            for (EnumFacing facing : this.getExits()) {
                int xPos = 0;
                int zPos = 0;
                switch (facing) {
                    case NORTH:
                        xPos = chunkPos.getXStart() + 7;
                        zPos = chunkPos.getZStart();
                        break;
                    case SOUTH:
                        xPos = chunkPos.getXStart() + 7;
                        zPos = chunkPos.getZEnd();
                        break;
                    case WEST:
                        xPos = chunkPos.getXStart();
                        zPos = chunkPos.getZStart() + 7;
                        break;
                    case EAST:
                        xPos = chunkPos.getXEnd();
                        zPos = chunkPos.getZStart() + 7;
                        break;
                    default:
                        break;
                }

                if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
                    world.setBlockState(new BlockPos(xPos, y, zPos), Blocks.GLOWSTONE.getDefaultState(), 2 | 16);
                }
            }
        }

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
}
