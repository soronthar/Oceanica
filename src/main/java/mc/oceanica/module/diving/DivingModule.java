package mc.oceanica.module.diving;

import mc.oceanica.Oceanica;
import mc.oceanica.module.diving.gear.ItemBuoyancyBelt;
import mc.oceanica.module.diving.gear.ItemMask;
import mc.oceanica.module.diving.tools.BlockAquaTorch;
import mc.oceanica.module.reef.block.BlockReefStone;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DivingModule {

    @GameRegistry.ObjectHolder(BlockAquaTorch.MOD_CONTEXT)
    public static BlockAquaTorch AQUA_TORCH;

    @GameRegistry.ObjectHolder(ItemMask.MOD_CONTEXT)
    public static Item ITEM_MASK;

    @GameRegistry.ObjectHolder(ItemBuoyancyBelt.MOD_CONTEXT)
    public static Item ITEM_BUOYANCY_BELT;

    public static void preInit(FMLPreInitializationEvent e) { }


    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockAquaTorch());
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(AQUA_TORCH.getItem());
        event.getRegistry().register(new ItemMask());
        event.getRegistry().register(new ItemBuoyancyBelt());
    }

    //TODO: fix bauble rendering. Make new textures
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(ITEM_MASK,0,new ModelResourceLocation(ITEM_MASK.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ITEM_BUOYANCY_BELT,0,new ModelResourceLocation(ITEM_BUOYANCY_BELT.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AQUA_TORCH),0,new ModelResourceLocation(AQUA_TORCH.getRegistryName(), "inventory"));
    }
}
