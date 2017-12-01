package mc.oceanica.module.diving.gear;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import mc.oceanica.OceanicaConfig;
import mc.oceanica.OceanicaInfo;
import mc.oceanica.core.ItemBauble;
import mc.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static mc.oceanica.OceanicaInfo.MODID;

//TODO: have two different masks, one with Snorkel and one without it
//TODO: Different Tiers for different depths
@Mod.EventBusSubscriber
public class ItemMask extends ItemBauble {
    public static final String REGISTRY_NAME = "diving.mask";
    public static final String MOD_CONTEXT = OceanicaInfo.MODID + ":"+ REGISTRY_NAME;

    public ItemMask() {
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

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onFogRender(EntityViewRenderEvent.FogDensity event) {

        EntityPlayer player =  Minecraft.getMinecraft().player;

        BaubleType baubleType = BaubleType.HEAD;
        ItemMask item = this;
        boolean equals = EntityUtil.hasBaubleInSlot(player, baubleType, item);
        if (event.getEntity() == player && equals) {
            if (EntityUtil.isInWater(player)) {
                event.setDensity(0.01F);
                //When exiting and reentering the water, FogMode is set to LINEAR
                //which makes everything visible up to the horizon.
                //This hack to set it to EXP mode fixes that, there is no way to do it using forge hooks.
                //Check EntityRender#setupFog for more info
                GlStateManager.setFog(GlStateManager.FogMode.EXP);
                event.setCanceled(true);
            }
        }
    }

}
