package mc.oceanica.module.diving.gear;

import baubles.api.BaubleType;
import mc.oceanica.OceanicaConfig;
import mc.oceanica.OceanicaInfo;
import mc.oceanica.core.ItemBauble;
import mc.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static mc.oceanica.OceanicaInfo.MODID;

//TODO: Different Tiers for different depths

public class ItemSnorkelMask extends ItemBauble {
    public static final String REGISTRY_NAME = "diving.snorkel.mask";
    public static final String MOD_CONTEXT = OceanicaInfo.MODID + ":"+ REGISTRY_NAME;

    public ItemSnorkelMask() {
        super(MODID, REGISTRY_NAME,BaubleType.HEAD);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (EntityUtil.isInWater(player)) {
            if (player.getEntityWorld().getSeaLevel() - player.getPosition().getY() < OceanicaConfig.diving.snorkelDepth && player.getAir() < 300) {
                if (player.getAir() < 200) {
                    player.playSound(SoundEvents.ENTITY_SQUID_AMBIENT, 0.4F, 1.2F);
                }
                player.setAir(300);
            }
        }
    }


}
