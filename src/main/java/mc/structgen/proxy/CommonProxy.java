package mc.structgen.proxy;

import mc.structgen.StructGenLib;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class CommonProxy {
    public void postInit(FMLPostInitializationEvent e) {
        StructGenLib.instance.getStructureManager().loadStructurePack("structgen/default_structure_pack");
    }
}
