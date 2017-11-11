package mc.structgen;

import mc.structgen.command.RegenChunkCommand;
import mc.structgen.command.SpawnStructureCommand;
import mc.structgen.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import static mc.structgen.StructGenLibInfo.MODID;
import static mc.structgen.StructGenLibInfo.MODVERSION;
import static mc.structgen.StructGenLibInfo.PACKAGE;


@Mod(
        modid = MODID,
        version = MODVERSION,
//        dependencies = "required-after:Forge@[14.21.1.2387,)",
        useMetadata = true
)
public class StructGenLib {
//TODO: multiple structure info in the same file, loaded on startup
// TODO: reload structure info
//TODO: sgen:raw command, spawn local or mod structures without structureInfo

    @SidedProxy(clientSide = PACKAGE + ".proxy.ClientProxy", serverSide = PACKAGE + ".proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static StructGenLib instance;

    public static Logger logger;

    private StructureManager structureManager=new StructureManager();

    public StructureManager getStructureManager() {
        return structureManager;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new RegenChunkCommand());
        event.registerServerCommand(new SpawnStructureCommand());
    }


}
