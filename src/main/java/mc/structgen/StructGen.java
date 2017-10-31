package mc.structgen;

import mc.structgen.command.SpawnStructureCommand;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

/**
 * Created by H440 on 30/10/2017.
 */
public class StructGen {
    public static void generateStructure(World world, BlockPos spawnPosition, ResourceLocation resourceLocation) {
        if (world.isRemote) return;

        WorldServer worldserver = (WorldServer) world;
        MinecraftServer minecraftserver = world.getMinecraftServer();
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        Template template = templatemanager.getTemplate(minecraftserver,resourceLocation);

        Mirror mirror = Mirror.NONE;
        Rotation rotation = Rotation.NONE;

//        for (String arg : args) {
//            if (arg.startsWith("R:")) {
//                rotation = Rotation.valueOf(arg.substring(2));
//            } else if (arg.startsWith("M:")) {
//                mirror = Mirror.valueOf(arg.substring(2));
//            }
//        }

        PlacementSettings placementsettings = (new PlacementSettings()).setMirror(mirror)
                .setRotation(rotation).setIgnoreEntities(false).setChunk((ChunkPos) null)
                .setReplacedBlock((Block) null).setIgnoreStructureBlock(false);


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

        //TODO: play with rotation
        template.addBlocksToWorld(world, spawnPosition, placementsettings, 2);
    }
}
