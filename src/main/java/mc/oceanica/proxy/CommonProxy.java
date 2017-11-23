package mc.oceanica.proxy;

import mc.oceanica.Oceanica;
import mc.oceanica.OceanicaConfig;
import mc.oceanica.OceanicaInfo;
import mc.oceanica.module.diving.DivingModule;
import mc.oceanica.module.reef.ReefModule;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;

@Mod.EventBusSubscriber(modid = OceanicaInfo.MODID)
public class CommonProxy {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ReefModule.registerBlocks(event);
        DivingModule.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ReefModule.registerItems(event);
        DivingModule.registerItems(event);
    }


    @SubscribeEvent
    public void playerSpawnEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (!OceanicaConfig.isDebug) return;

//        EntityLivingBase player=event.player;
//        if (player.getEntityWorld()!=null && !player.getEntityWorld().isRemote) {
//            PotionEffect effect = player.getActivePotionEffect(MobEffects.WATER_BREATHING);
//            if (effect == null) {
//                PotionEffect neweffect = new PotionEffect(MobEffects.WATER_BREATHING, Integer.MAX_VALUE, -42, true, false);
//                player.addPotionEffect(neweffect);
//            }
//            effect = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
//            if (effect == null) {
//                PotionEffect neweffect = new PotionEffect(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, -42, true, false);
//                player.addPotionEffect(neweffect);
//            }
//        }
    }

    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();

//        OceanicaConfig.readConfig(new File(directory.getPath(), OceanicaInfo.MODID+".cfg"), true);
        ReefModule.preInit(e);
        DivingModule.preInit(e);
    }

    public void init(FMLInitializationEvent e) {
//        NetworkRegistry.INSTANCE.registerGuiHandler(ModTut.instance, new GuiProxy());
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void postInit(FMLPostInitializationEvent e) {
//        OceanicaConfig.save();
    }


}