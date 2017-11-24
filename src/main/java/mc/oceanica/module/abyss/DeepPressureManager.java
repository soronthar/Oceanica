package mc.oceanica.module.abyss;

import mc.oceanica.OceanicaConfig;
import mc.oceanica.OceanicaInfo;
import mc.util.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static mc.util.EntityUtil.hasArmor;

@Mod.EventBusSubscriber(modid = OceanicaInfo.MODID)
public class DeepPressureManager {
    private static final DamageSource OCEAN_PRESSURE_DAMAGE = new DamageSource("pressure").setDamageBypassesArmor().setDamageIsAbsolute();

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!OceanicaConfig.abyss.deepPressure.enableDeepPressure) return;

        EntityPlayer player = event.player;
        BlockPos pos = player.getPosition();
        World world = player.getEntityWorld();
        if (BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.OCEAN) ||
                BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.BEACH)) {

            int seaLevel = world.getSeaLevel();

            if (EntityUtil.isInWater(player) && !OCEAN_PRESSURE_DAMAGE.equals(player.getLastDamageSource())) {
                int bandHeight = seaLevel / 4;
                int totalArmorValue = player.getTotalArmorValue();
                if (pos.getY() <= seaLevel && pos.getY() > bandHeight * 3) {
                    //No damage at this level. I´m not a sadist.
                } else if (pos.getY() <= bandHeight * 3 && pos.getY() > bandHeight * 2 && totalArmorValue < 7) {
                    player.attackEntityFrom(OCEAN_PRESSURE_DAMAGE, 2.0F);
                } else if (pos.getY() <= bandHeight * 2 && pos.getY() > bandHeight && totalArmorValue < 15) {
                    player.attackEntityFrom(OCEAN_PRESSURE_DAMAGE, 4.0F);
                } else if (pos.getY() <= bandHeight && pos.getY() > 0 && totalArmorValue < 20) {
                    player.attackEntityFrom(OCEAN_PRESSURE_DAMAGE, 6.0F);
                }
            }
        }
        event.setResult(Event.Result.DEFAULT);
    }
}
