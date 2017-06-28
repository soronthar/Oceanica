package mc.oceanica.proxy;

import mc.oceanica.OceanicaInfo;
import mc.oceanica.module.diving.DivingModule;
import mc.oceanica.module.reef.ReefModule;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        OBJLoader.INSTANCE.addDomain(OceanicaInfo.MODID);
//        ModelLoaderRegistry.registerLoader(new BakedModelLoader());

//        // Typically initialization of models and such goes here:
//        ModBlocks.initModels();
//        ModItems.initModels();
//        ModEntities.initModels();
        ReefModule.initModels();
        DivingModule.initModels();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);

//        // Initialize our input handler so we can listen to keys
//        MinecraftForge.EVENT_BUS.register(new InputHandler());
//        KeyBindings.init();
//
//        ModBlocks.initItemModels();
    }
}