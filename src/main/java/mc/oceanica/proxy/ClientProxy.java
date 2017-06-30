package mc.oceanica.proxy;

import mc.oceanica.OceanicaInfo;
import mc.oceanica.module.diving.DivingModule;
import mc.oceanica.module.reef.ReefModule;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ReefModule.registerModels(event);
        DivingModule.registerModels(event);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
    }
}