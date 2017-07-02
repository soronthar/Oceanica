package mc.oceanica.module.reef;

import mc.oceanica.Oceanica;
import mc.oceanica.module.reef.block.BlockCoral;
import mc.oceanica.module.reef.block.BlockReefStone;
import mc.oceanica.module.reef.world.LeavesMarkWorldGenerator;
import mc.oceanica.module.reef.world.ReefWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ReefModule {

    @GameRegistry.ObjectHolder(BlockReefStone.MOD_CONTEXT)
    public static BlockReefStone REEF_STONE;

    @GameRegistry.ObjectHolder(BlockCoral.MOD_CONTEXT)
    public static BlockCoral CORAL;


    public static void preInit(FMLPreInitializationEvent e) {
        GameRegistry.registerWorldGenerator(new ReefWorldGenerator(), 1);
//        GameRegistry.registerWorldGenerator(new LeavesMarkWorldGenerator(), 2);
    }

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockReefStone());
        event.getRegistry().register(new BlockCoral());
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(REEF_STONE.getItem());
        registry.register(CORAL.getItem());
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(REEF_STONE), 0, new ModelResourceLocation(REEF_STONE.getRegistryName(), "inventory"));

        String registryName = CORAL.getRegistryName().toString();
        Item itemFromBlock = Item.getItemFromBlock(CORAL);
        for (EnumDyeColor dyeColor : EnumDyeColor.values()) {
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(registryName + "."+dyeColor.getUnlocalizedName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(itemFromBlock, dyeColor.getMetadata(), modelResourceLocation);
        }


    }

}
