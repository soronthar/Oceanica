package mc.oceanica.module.abyss;

import mc.oceanica.OceanicaConfig;
import mc.oceanica.OceanicaInfo;
import mc.oceanica.module.abyss.world.TrenchGenerator;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = OceanicaInfo.MODID)
public class AbyssModule {

    private static TrenchGenerator trenchGenerator = new TrenchGenerator();


    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onChunkReplaceBlocks(ChunkGeneratorEvent.ReplaceBiomeBlocks event) {
        if (OceanicaConfig.abyss.trenches.enableTrenches) {
            trenchGenerator.generate(event.getWorld(), event.getX(), event.getZ(), event.getPrimer());
        }
        event.setResult(Event.Result.DEFAULT);
    }



}
