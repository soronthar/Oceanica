package mc.oceanica.module.abyss;

import mc.oceanica.Oceanica;
import mc.oceanica.OceanicaInfo;
import mc.oceanica.module.abyss.world.TrenchGenerator;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = OceanicaInfo.MODID)
public class AbyssModule {


    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onChunkReplaceBlocks(ChunkGeneratorEvent.ReplaceBiomeBlocks event) {
        TrenchGenerator trenchGenerator = new TrenchGenerator();
        trenchGenerator.generate(event.getWorld(),event.getX(), event.getZ(),event.getPrimer());
        event.setResult(Event.Result.DEFAULT);
    }
}
