package mc.oceanica.module.reef;

import mc.oceanica.Oceanica;
import mc.oceanica.module.reef.block.BlockCoral;
import mc.oceanica.module.reef.block.BlockReefStone;
import mc.oceanica.module.reef.world.LeavesMarkWorldGenerator;
import mc.oceanica.module.reef.world.ReefWorldGenerator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ReefModule {
    public static BlockReefStone REEF_STONE;
    public static BlockCoral CORAL;


    public static void preInit(FMLPreInitializationEvent e) {
        GameRegistry.registerWorldGenerator(new ReefWorldGenerator(), 1);
        GameRegistry.registerWorldGenerator(new LeavesMarkWorldGenerator(), 2);

        REEF_STONE = new BlockReefStone();
        CORAL = new BlockCoral();

    }


    @SideOnly(Side.CLIENT)
    public static void initModels() {
        if (REEF_STONE == null) {
            Oceanica.logger.error("Reef Stone was not correctly initialized. The model was not loaded.");
        } else {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(REEF_STONE), 0, new ModelResourceLocation(REEF_STONE.getRegistryName(), "inventory"));
        }

        if (CORAL == null) {
            Oceanica.logger.error("Reef Stone was not correctly initialized. The model was not loaded.");
        } else {
            String registryName = CORAL.getRegistryName().toString();
            Item itemFromBlock = Item.getItemFromBlock(CORAL);
            for (EnumDyeColor dyeColor : EnumDyeColor.values()) {
                ModelResourceLocation modelResourceLocation = new ModelResourceLocation(registryName + "."+dyeColor.getUnlocalizedName(), "inventory");
                ModelLoader.setCustomModelResourceLocation(itemFromBlock, dyeColor.getMetadata(), modelResourceLocation);
            }
        }


    }

}
