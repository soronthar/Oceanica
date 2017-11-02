package mc.structgen.command;

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

/**
 * Created by H440 on 29/10/2017.
 */
public class SpawnStructureCommand extends CommandBase {
    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "spawn";
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

        TemplateManager templatemanager = world.getStructureTemplateManager();
        ResourceLocation loc = new ResourceLocation("structgen", "debug/smallring");
//        ResourceLocation loc = new ResourceLocation(MODID, "teststructure");
        Template template = templatemanager.getTemplate(server, loc);

        Mirror mirror = Mirror.NONE;
        Rotation rotation = Rotation.NONE;

        for (String arg : args) {
            if (arg.startsWith("R:")) {
                rotation = Rotation.valueOf(arg.substring(2));
            } else if (arg.startsWith("M:")) {
                mirror = Mirror.valueOf(arg.substring(2));
            }
        }

        PlacementSettings placementsettings = (new PlacementSettings()).setMirror(mirror)
                .setRotation(rotation).setIgnoreEntities(false).setChunk((ChunkPos) null)
                .setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

        BlockPos spawnPosition = commandSenderEntity.getPosition();
        switch (rotation) {
            case CLOCKWISE_90:
                spawnPosition = spawnPosition.east(15);
                break;
            case CLOCKWISE_180:
                spawnPosition = spawnPosition.east(15);
                spawnPosition = spawnPosition.south(15);
                break;
            case COUNTERCLOCKWISE_90:
                spawnPosition = spawnPosition.south(15);
                break;
            default:
                break;
        }

        template.addBlocksToWorld(world, spawnPosition.up(3), new SomeProcessing(), placementsettings, 2);

    }


    private class SomeProcessing implements ITemplateProcessor {
        @Nullable
        @Override
        public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
            if (blockInfoIn.blockState.getBlock().equals(Blocks.WOOL)) {
                return new Template.BlockInfo(blockInfoIn.pos, Blocks.WOOL.getStateFromMeta(EnumDyeColor.RED.getMetadata()), blockInfoIn.tileentityData);
            } else {
                return blockInfoIn;
            }
        }
    }
}
