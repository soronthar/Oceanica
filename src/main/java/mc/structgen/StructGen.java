package mc.structgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class StructGen {
    private static Map<String, StructureInfo> structureCache = new HashMap<>();

    public static void generateStructure(World world, BlockPos spawnPosition, ResourceLocation resourceLocation) {
        generateStructure(world, spawnPosition, new StructureInfo(resourceLocation));
    }

    public static void generateStructure(World world, BlockPos spawnPosition, StructureInfo info) {
        generateStructure(world, spawnPosition, info, Rotation.NONE);
    }

    public static void generateStructure(World world, BlockPos spawnPosition, StructureInfo info, Rotation rotation) {
        //TODO: Chests and trapped chests
        //TODO: Spawners
        //TODO: Spawn mobs.. bosses... etc
        if (world.isRemote) return;
        if (info == null || spawnPosition == null) return;

        Template template = getTemplate(world, info);
        PlacementSettings placementsettings = getPlacementSettings(rotation, Mirror.NONE);
        spawnPosition = rotateInPlace(spawnPosition, rotation);
        placeStructureInWorld(template, info.getPalette(), spawnPosition, placementsettings, world);
    }

    private static void placeStructureInWorld(Template template, BlockPalette palette, BlockPos spawnPosition, PlacementSettings placementsettings, World world) {
        if (palette != null) {
            template.addBlocksToWorld(world, spawnPosition, new BlockPaletteTemplateProcessor(palette), placementsettings, 2);
        } else {
            template.addBlocksToWorld(world, spawnPosition, placementsettings, 2);
        }
    }

    private static Template getTemplate(World world, StructureInfo info) {
        WorldServer worldserver = (WorldServer) world;
        MinecraftServer minecraftserver = world.getMinecraftServer();
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        return templatemanager.getTemplate(minecraftserver, info.getResourceLocation());
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

    public static StructureInfo loadStructureInfo(String structureName) {
        return structureCache.computeIfAbsent(structureName, StructGen::createStructureInfo);
    }

    private static StructureInfo createStructureInfo(String structureName) {
        StructureInfo info = null;
        JsonObject structureInfoJson = readStructureInfoFile(structureName);

        if (structureInfoJson!=null) {
            String name = structureInfoJson.get("name").getAsString();
            //TODO: structure variations
            //TODO: Additional structure configurations (like.. conditions)
            JsonArray structure = structureInfoJson.get("structure").getAsJsonArray();
            String structurePath = structure.get(0).getAsString();
            BlockPalette palette = createPalette(structureName, structureInfoJson);

            info = new StructureInfo(name,new ResourceLocation(structurePath), palette);
        }

        return info;
    }

    private static BlockPalette createPalette(String structureName, JsonObject structureInfoJson) {
        //TODO: weighted palette
        JsonObject paletteJson = structureInfoJson.get("palette").getAsJsonObject();
        BlockPalette palette;
        if (paletteJson != null) {
            palette = new BlockPalette();

            for (Map.Entry<String, JsonElement> next : paletteJson.entrySet()) {
                String originalBlock = next.getKey();
                String transformedBlock = next.getValue().getAsString();

                IBlockState originalState = getBlockState(originalBlock);
                IBlockState transformedState = getBlockState(transformedBlock);

                if (originalState == null) { //TODO: Proper logging
                    System.out.printf("Error reading structure %s palette: Unrecognized Block %s", structureName, originalBlock);
                }
                if (transformedState == null) { //TODO: Proper logging
                    System.out.printf("Error reading structure %s palette: Unrecognized Block %s", structureName, transformedBlock);
                }

                palette.addTransform(originalState, transformedState);
            }
        } else {
            palette = BlockPalette.DEFAULT;
        }
        return palette;
    }

    private static JsonObject readStructureInfoFile(String structureName) {
        try (
                InputStream inputstream = StructGen.class.getResourceAsStream("/assets/" + structureName + ".json"); //todo: multiple assets sources
                BufferedReader br = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
        ) {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(br);
            return element.getAsJsonObject();
        } catch (IOException e) {
            System.out.println("Error reading " + structureName);
            e.printStackTrace();
            return null;
        }
    }

    private static IBlockState getBlockState(String blockName) {
        String[] split = blockName.split("@");
        Block block = Block.getBlockFromName(split[0]);
        if (block == null) return null;

        IBlockState state;
        if (split.length > 1) {
            state = block.getStateFromMeta(Integer.parseInt(split[1]));
        } else {
            state = block.getDefaultState();
        }
        return state;
    }

}
