package mc.structgen.command;

import mc.structgen.*;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.annotation.Nullable;

public class SpawnStructureCommand extends CommandBase {
    @Override
    public String getName() {
        return "sgen:spawn";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "sgen:spawn [modid:]structure-name [c:x,y,z] [p:palette] [r:rotation]";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        EntityPlayer commandSenderEntity = (EntityPlayer) sender.getCommandSenderEntity();
        return commandSenderEntity != null && commandSenderEntity.isCreative();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        StructureManager structureManager = StructGenLib.instance.getStructureManager();

        EntityPlayer commandSenderEntity = (EntityPlayer) sender.getCommandSenderEntity();
        BlockPos senderPosition = sender.getPosition();

        int dimension = sender.getEntityWorld().provider.getDimension();
        WorldServer world = server.getWorld(dimension);

        Mirror mirror = Mirror.NONE;
        Rotation rotation = Rotation.NONE;
        BlockPos spawnPos = senderPosition;
        String structureName = null;
        BlockPalette palette=null;
        for (String arg : args) {
            if (arg.startsWith("R:") || arg.startsWith("r:")) {
                rotation = Rotation.valueOf(arg.substring(2));
            } else if (arg.startsWith("P:") || arg.startsWith("p:")) {
                palette = structureManager.getBlockPalette(arg.substring(2));
            } else if (arg.startsWith("C:") || arg.startsWith("c:")) {
                String[] posString = arg.substring(2).split(",");
                if (posString.length == 3) {
                    int x = getCoordFromParam(posString[0], spawnPos.getX());
                    int y = getCoordFromParam(posString[1], spawnPos.getY());
                    int z = getCoordFromParam(posString[2], spawnPos.getZ());
                    spawnPos = new BlockPos(x, y, z);
                }
            } else {
                structureName = arg;

//                    if (arg.contains(":")) {
//                        structureName=arg;
//                    } else {
//                        structureName= StructGenLibInfo.MODID+":"+arg;
//                    }
            }
        }

        if (structureName != null) {
            StructureInfo structureInfo = structureManager.getStructureInfo(structureName);
            StructGen.generateStructure(world, spawnPos, structureInfo, rotation, palette);

        }
    }

    private int getCoordFromParam(String param, int defaultValue) {
        if (param.equals("~")) {
            return defaultValue;
        } else if (param.startsWith("+") || param.startsWith("-")) {
            return defaultValue + Integer.parseInt(param);
        } else {
            return Integer.parseInt(param);
        }
    }


}
