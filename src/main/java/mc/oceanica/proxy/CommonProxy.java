package mc.oceanica.proxy;

import mc.oceanica.OceanicaConfig;
import mc.oceanica.OceanicaInfo;
import mc.oceanica.module.diving.DivingModule;
import mc.oceanica.module.reef.ReefModule;
import mc.oceanica.module.reef.world.ReefWorldGenerator;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

public class CommonProxy {

    // Config instance
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), OceanicaInfo.MODID+".cfg"),OceanicaInfo.MODVERSION,true);
        OceanicaConfig.readConfig();
        ReefModule.preInit(e);
        DivingModule.preInit(e);

//        // Initialize our packet handler. Make sure the name is
//        // 20 characters or less!
//        PacketHandler.registerMessages("modtut");
//
//        // Initialization of blocks and items typically goes here:
//        ModBlocks.init();
//        ModItems.init();
//        ModEntities.init();
//        ModDimensions.init();
//
//        MainCompatHandler.registerWaila();
//        MainCompatHandler.registerTOP();

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