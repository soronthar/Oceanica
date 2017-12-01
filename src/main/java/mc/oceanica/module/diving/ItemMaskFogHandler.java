package mc.oceanica.module.diving;

import baubles.api.BaubleType;
import mc.oceanica.OceanicaInfo;
import mc.oceanica.module.diving.DivingModule;
import mc.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = OceanicaInfo.MODID)
public class ItemMaskFogHandler {
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (event.getEntity() == player &&
                EntityUtil.hasBaubleInSlot(player, BaubleType.HEAD, DivingModule.ITEM_MASK, DivingModule.ITEM_SNORKEL_MASK)) {
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
