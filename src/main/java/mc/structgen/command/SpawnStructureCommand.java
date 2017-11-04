package mc.structgen.command;

import mc.structgen.StructGen;
import mc.structgen.StructureInfo;
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

//TODO: Redefine this command
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
        EntityPlayer commandSenderEntity = (EntityPlayer) sender.getCommandSenderEntity();

        int dimension = sender.getEntityWorld().provider.getDimension();
        WorldServer world = server.getWorld(dimension);

        Mirror mirror = Mirror.NONE;
        Rotation rotation = Rotation.NONE;

        for (String arg : args) {
            if (arg.startsWith("R:")) {
                rotation = Rotation.valueOf(arg.substring(2));
            } else if (arg.startsWith("M:")) {
                mirror = Mirror.valueOf(arg.substring(2));
            }
        }

        //TODO: parameter handling
        StructureInfo structureInfo = StructGen.loadStructureInfo("structgen:bighollowring");
        StructGen.generateStructure(world,sender.getPosition(),structureInfo,rotation);


    }



}
