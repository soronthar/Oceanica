package mc.oceanica.module.diving;

import mc.oceanica.Oceanica;
import mc.oceanica.module.diving.gear.ItemMask;
import mc.oceanica.module.diving.tools.BlockAquaTorch;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DivingModule {
    public static BlockAquaTorch ACUA_TORCH;
    public static Item ITEM_MASK;

    public static void preInit(FMLPreInitializationEvent e) {
        ITEM_MASK =new ItemMask();
        ACUA_TORCH=new BlockAquaTorch();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        if (ITEM_MASK==null) {
            Oceanica.logger.error("Diving Mask was not correctly initialized. The model was not loaded.");
        } else {
            ModelLoader.setCustomModelResourceLocation(ITEM_MASK,0,new ModelResourceLocation(ITEM_MASK.getRegistryName(), "inventory"));
        }

        if (ACUA_TORCH==null) {
            Oceanica.logger.error("AcuaTorch was not correctly initialized. The model was not loaded.");
        } else {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ACUA_TORCH),0,new ModelResourceLocation(ACUA_TORCH.getRegistryName(), "inventory"));
        }
    }

}
