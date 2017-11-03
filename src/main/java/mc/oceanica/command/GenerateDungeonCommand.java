package mc.oceanica.command;

import mc.oceanica.OceanicaConfig;
import mc.oceanica.module.abyss.world.dungeon.map.DungeonMap;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static mc.util.Utils.addVec3i;

/**
 * Created by H440 on 01/11/2017.
 */
public class GenerateDungeonCommand extends CommandBase {
    @Override
    public String getName() {
        return "gendungeon";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "gendungeon radius NORT|SOUTH EAST|WEST";
    }
    DungeonMap dungeonMap;


    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        //TODO update when dungeon-generating code is hooked to the structure generation
        int radius=8;
        EnumFacing zAxis=EnumFacing.NORTH;
        EnumFacing xAxis=EnumFacing.EAST;
        if (args.length>0) {
            radius=Integer.parseInt(args[0]);
        }
        if (args.length>1) {
            zAxis=EnumFacing.valueOf(args[1]);
        }
        if (args.length>2) {
            xAxis=EnumFacing.valueOf(args[2]);
        }

        BlockPos position = sender.getPosition();

        ChunkPos referenceChunk = new ChunkPos(position);
        Random rand=new Random();
        Vec3i facing = addVec3i(zAxis.getDirectionVec(), xAxis.getDirectionVec());
        if (dungeonMap==null ||
                radius!=dungeonMap.getRadius() ||
                !facing.equals(dungeonMap.getFacing())){
//                ||
//                !referenceChunk.equals(dungeonMap.getStartChunk())) {
            dungeonMap = new DungeonMap(referenceChunk, radius, facing);
            dungeonMap.generateMap(rand);
        }

        ChunkPos spawnChunk = dungeonMap.getStartChunk();
        ChunkPos cornerChunk = new ChunkPos(spawnChunk.x + (radius * facing.getX()), spawnChunk.z + (radius * facing.getZ()));
        int minX=Math.min(spawnChunk.x,cornerChunk.x);
        int minZ=Math.min(spawnChunk.z,cornerChunk.z);
        int maxX=Math.max(spawnChunk.x,cornerChunk.x);
        int maxZ=Math.max(spawnChunk.z,cornerChunk.z);

        for(int x=minX;x<=maxX;x++) {
            for(int z=minZ;z<=maxZ;z++) {
                ChunkPos currentChunk = new ChunkPos(x,z);
                if (dungeonMap.contains(currentChunk)) {
                    dungeonMap.drawRoomAt(currentChunk, 64, sender.getEntityWorld());
//                    dungeonMap.drawRoomAt(currentChunk, position.getY(), sender.getEntityWorld());
                }
            }
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.<String>asList("oc:gd");
    }
}
