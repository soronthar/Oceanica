package mc.structgen;

import mc.structgen.command.GenerateDungeonCommand;
import mc.structspawn.StructSpawnLibInfo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import static mc.structgen.StructGenModInfo.MODID;
import static mc.structgen.StructGenModInfo.MODVERSION;


@Mod(
        modid = MODID,
        version = MODVERSION,
        useMetadata = true
)
//TODO: Add dependency to StructSpawnLib
public class StructGenMod {
    @Mod.Instance
    public static StructGenMod instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLInterModComms.sendMessage(StructSpawnLibInfo.MODID,"registerStructurePack","dungeon/dungeon_parts");
        logger = event.getModLog();
    }


    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new GenerateDungeonCommand());

    }
}
