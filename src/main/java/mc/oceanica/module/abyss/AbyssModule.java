package mc.oceanica.module.abyss;

import mc.oceanica.OceanicaConfig;
import mc.oceanica.OceanicaInfo;
import mc.oceanica.module.abyss.world.TrenchGenerator;
import mc.util.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static mc.util.EntityUtil.hasArmor;

@Mod.EventBusSubscriber(modid = OceanicaInfo.MODID)
public class AbyssModule {
    private static final DamageSource OCEAN_PRESSURE_DAMAGE=new DamageSource("pressure").setDamageBypassesArmor().setDamageIsAbsolute();
    private static TrenchGenerator trenchGenerator = new TrenchGenerator();


    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onChunkReplaceBlocks(ChunkGeneratorEvent.ReplaceBiomeBlocks event) {
        if (OceanicaConfig.enableTrenches) {
            trenchGenerator.generate(event.getWorld(), event.getX(), event.getZ(), event.getPrimer());
        }
        event.setResult(Event.Result.DEFAULT);
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        //TODO: check this only for oceans and stuff
//        EntityPlayer player = event.player;
//        BlockPos position = player.getPosition();
//        World entityWorld = player.getEntityWorld();
//        int seaLevel = entityWorld.getSeaLevel();
//
//
//
//        if (EntityUtil.isInWater(player)) {
//            if (position.getY() < seaLevel  && position.getY() > (seaLevel/4) * 3) {
//                if (!hasArmor(player, EntityEquipmentSlot.CHEST, Items.LEATHER_CHESTPLATE)) {
//                    if (!OCEAN_PRESSURE_DAMAGE.equals(player.getLastDamageSource())) {
//                        player.attackEntityFrom(OCEAN_PRESSURE_DAMAGE, 2.0F);
//                    }
//                }
//            } else if (position.getY() < (seaLevel/4) * 3  && position.getY() > (seaLevel/4) * 2) {
//                if (!hasArmor(player, EntityEquipmentSlot.CHEST, Items.IRON_CHESTPLATE)) {
//                    if (!OCEAN_PRESSURE_DAMAGE.equals(player.getLastDamageSource())) {
//                        player.attackEntityFrom(OCEAN_PRESSURE_DAMAGE, 4.0F);
//                    }
//                }
//
//            } else if (position.getY() < (seaLevel/4) * 2  && position.getY() > 1) {
//                if (!hasArmor(player, EntityEquipmentSlot.CHEST, Items.DIAMOND_CHESTPLATE)) {
//                    if (!OCEAN_PRESSURE_DAMAGE.equals(player.getLastDamageSource())) {
//                        player.attackEntityFrom(OCEAN_PRESSURE_DAMAGE, 6.0F);
//                    }
//                }
//            }
//
//
//        }
//        event.setResult(Event.Result.DEFAULT);
    }

}
