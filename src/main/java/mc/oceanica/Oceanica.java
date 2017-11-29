package mc.oceanica;

import mc.oceanica.command.PlayerInfoCommand;
import mc.oceanica.command.StatCommand;
import mc.oceanica.module.diving.DivingModule;
import mc.oceanica.module.reef.ReefModule;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import static mc.oceanica.OceanicaInfo.*;

/**
 * Main class of the mod.
 * It is mostly empty, as the initialization of items, blocks and models is done on each module.
 * Check the *Module classes for more details.
 */
@Mod(
        modid = MODID,
        version = MODVERSION,
        dependencies = "required-after:Forge@[14.23.0.2491,)",
        useMetadata = true
)
@Mod.EventBusSubscriber(modid = OceanicaInfo.MODID)
public class Oceanica {
    @Mod.Instance
    public static Oceanica instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        ReefModule.registerWorldGeneration();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) { }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new StatCommand(OceanicaStats.INSTANCE));
        event.registerServerCommand(new PlayerInfoCommand());
    }

//    @SubscribeEvent
//    public static void registerItems(RegistryEvent.Register<Item> event) {
//        ReefModule.registerItems(event);
//        DivingModule.registerItems(event);
//    }

}
