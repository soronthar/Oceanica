package mc.oceanica.proxy;

import mc.oceanica.OceanicaConfig;
import mc.oceanica.OceanicaInfo;
import mc.oceanica.module.diving.DivingModule;
import mc.oceanica.module.reef.ReefModule;
import mc.oceanica.module.reef.world.ReefWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {

    public static Configuration config;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ReefModule.registerBlocks(event);
        DivingModule.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ReefModule.registerItems(event);
        DivingModule.registerItems(event);
    }


    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), OceanicaInfo.MODID+".cfg"),OceanicaInfo.MODVERSION,true);
        OceanicaConfig.readConfig();
        ReefModule.preInit(e);
        DivingModule.preInit(e);
    }

    public void init(FMLInitializationEvent e) {
//        NetworkRegistry.INSTANCE.registerGuiHandler(ModTut.instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }
}