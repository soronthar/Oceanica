package mc.structspawn.manager;

import mc.structspawn.StructSpawnLib;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.Map;


public class StructSpawn {
    //TODO: remove sdtatic methods.. maybe?
    public static void generateStructure(World world, BlockPos spawnPosition, String structureName) {
        generateStructure(world, spawnPosition, structureName,Rotation.NONE,null);
    }

    public static void generateStructure(World world, BlockPos spawnPosition, String structureName, Rotation rotation, BlockPalette palette) {
        StructureInfo structureInfo = StructSpawnLib.instance.getStructurePackManager().getStructureInfo(structureName);
        generateStructure(world, spawnPosition, structureInfo , rotation, palette);
    }

    private static void generateStructure(World world, BlockPos spawnPosition, StructureInfo info, Rotation rotation, BlockPalette palette) {
        //TODO: Spawn mobs.. bosses... etc
        //TODO: Spawners
        if (world.isRemote) return;
        if (info == null || spawnPosition == null) return;

        Template template = getTemplate(world, info);
        PlacementSettings placementsettings = getPlacementSettings(rotation, Mirror.NONE);
        spawnPosition = rotateInPlace(spawnPosition, rotation);

        placeStructureInWorld(template, palette==null?info.getPalette():palette, spawnPosition, placementsettings, world);

    }

    private static void placeStructureInWorld(Template template, BlockPalette palette, BlockPos spawnPosition, PlacementSettings placementsettings, World world) {
        if (palette != null) {
            template.addBlocksToWorld(world, spawnPosition, new BlockPaletteTemplateProcessor(palette), placementsettings, 2);
        } else {
            template.addBlocksToWorld(world, spawnPosition, placementsettings, 2);
        }

        //TODO: define loot tables in the info
        //TODO: load the template when loading the info?
        Map<BlockPos, String> dataBlocks = template.getDataBlocks(spawnPosition, placementsettings);
        for (Map.Entry<BlockPos, String> entry : dataBlocks.entrySet()) {
            if ("chest".equals(entry.getValue())) {
                BlockPos key = entry.getKey();
                world.setBlockState(key,Blocks.CHEST.getDefaultState());
                TileEntity tileEntity = world.getTileEntity(key);
                if (tileEntity instanceof TileEntityChest) {
                    ((TileEntityChest)tileEntity).setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON,world.rand.nextLong());
                }
            }
        }
    }

    private static Template getTemplate(World world, StructureInfo info) {
        WorldServer worldserver = (WorldServer) world;
        MinecraftServer minecraftserver = world.getMinecraftServer();
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        return templatemanager.get(minecraftserver, info.getResourceLocation());
    }

    private static PlacementSettings getPlacementSettings(Rotation rotation, Mirror mirror) {
        return (new PlacementSettings()).setMirror(mirror)
                .setRotation(rotation).setIgnoreEntities(false).setIgnoreStructureBlock(false);
    }

    private static BlockPos rotateInPlace(BlockPos spawnPosition, Rotation rotation) {
        //TODO:Rotate using the size of the structure.
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
        return spawnPosition;
    }

}