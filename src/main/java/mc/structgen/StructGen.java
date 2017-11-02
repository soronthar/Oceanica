package mc.structgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mc.structgen.command.SpawnStructureCommand;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by H440 on 30/10/2017.
 */
public class StructGen {
    public static void generateStructure(World world, BlockPos spawnPosition, ResourceLocation resourceLocation) {
        generateStructure(world, spawnPosition, new StructureInfo(resourceLocation));
    }

    public static void generateStructure(World world, BlockPos spawnPosition, StructureInfo info) {
        generateStructure(world,spawnPosition,info,Rotation.NONE);
    }

    public static void generateStructure(World world, BlockPos spawnPosition, StructureInfo info, Rotation rotation) {
        if (world.isRemote) return;

        WorldServer worldserver = (WorldServer) world;
        MinecraftServer minecraftserver = world.getMinecraftServer();
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        Template template = templatemanager.getTemplate(minecraftserver,info.getResourceLocation());

        Mirror mirror = Mirror.NONE;

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

        if (info.getPalette()!=null) {
            template.addBlocksToWorld(world, spawnPosition, new BlockPaletteTemplateProcessor(info.getPalette()), placementsettings, 2);
        } else {
            template.addBlocksToWorld(world, spawnPosition, placementsettings, 2);
        }

    }

    public static StructureInfo loadStructureInfo(String structureName) {
        BlockPalette palette = new BlockPalette();
        StructureInfo info=null;
//TODO: cache this info
        try { //TODO consider empty tags and errors in the file.
            InputStream inputstream = StructGen.class.getResourceAsStream("/assets/"+structureName + ".json"); //todo:add/remove slash
            BufferedReader br = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(br);
            JsonObject asJsonObject = element.getAsJsonObject();
            String name = asJsonObject.get("name").getAsString();
            JsonArray structure = asJsonObject.get("structure").getAsJsonArray();
            String structurePath = structure.get(0).getAsString();

            //TODO: weighted palette
            JsonObject jsonObject = asJsonObject.get("palette").getAsJsonObject();
            for (Map.Entry<String, JsonElement> next : jsonObject.entrySet()) {
                String originalBlock=next.getKey();
                String transformedBlock=next.getValue().getAsString();

                IBlockState originalState = getBlockState(originalBlock);
                IBlockState transformedState = getBlockState(transformedBlock);

                if (originalState==null) { //TODO: Proper logging
                    System.out.printf("Error reading structure %s palette: Unrecognized Block %s", structureName,originalBlock );
                }
                if (transformedState==null) { //TODO: Proper logging
                    System.out.printf("Error reading structure %s palette: Unrecognized Block %s", structureName,transformedBlock );
                }

                palette.addTransform(originalState,transformedState);
            }
            info = new StructureInfo(new ResourceLocation(structurePath), palette);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }



        return info;
    }

    private static IBlockState getBlockState(String blockName) {
        String[] split = blockName.split("@");
        Block block = Block.getBlockFromName(split[0]);
        if (block==null) return null;

        IBlockState state;
        if (split.length>1) {
            state=block.getStateFromMeta(Integer.parseInt(split[1]));
        } else {
            state=block.getDefaultState();
        }
        return state;
    }

    private static class BlockPaletteTemplateProcessor implements ITemplateProcessor {
        BlockPalette palette;

        public BlockPaletteTemplateProcessor(BlockPalette palette) {
            this.palette = palette;
        }

        @Nullable
        @Override
        public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
            IBlockState newBlockState;
//            if (blockInfoIn.blockState.getBlock().equals(Blocks.AIR)) { //Make this block configurable
//                newBlockState = worldIn.getBlockState(pos);
//            } else {
                newBlockState = palette.transform(blockInfoIn.blockState);
//            }
            return new Template.BlockInfo(blockInfoIn.pos,newBlockState,blockInfoIn.tileentityData);
        }
    }

}
