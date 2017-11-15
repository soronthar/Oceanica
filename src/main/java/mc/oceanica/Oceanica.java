package mc.oceanica;

import mc.oceanica.command.GenerateDungeonCommand;
import mc.structspawn.StructSpawnLib;
import mc.structspawn.StructSpawnLibInfo;
import mc.structspawn.command.RegenChunkCommand;
import mc.oceanica.command.RegenChunkNoReefCommand;
import mc.oceanica.command.StatCommand;
import mc.oceanica.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import org.apache.logging.log4j.Logger;

import static mc.oceanica.OceanicaInfo.*;

@Mod(
        modid = MODID,
        version = MODVERSION,
        dependencies = "required-after:Forge@[14.21.1.2387,)",
        useMetadata = true
)
public class Oceanica {
    @SidedProxy(clientSide = PACKAGE+".proxy.ClientProxy", serverSide = PACKAGE+".proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Oceanica instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        FMLInterModComms.sendMessage(StructSpawnLibInfo.MODID,"registerStructurePack","dungeon/dungeon_parts");
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) { proxy.postInit(e); }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new RegenChunkCommand());
        event.registerServerCommand(new RegenChunkNoReefCommand());
        event.registerServerCommand(new StatCommand(OceanicaStats.INSTANCE));
        //These do not belong to Oceanica
        event.registerServerCommand(new GenerateDungeonCommand());
    }
}
