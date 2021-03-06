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

//TODO: Different Tiers for different depths
@Mod.EventBusSubscriber
public class ItemMask extends ItemBauble {
    public static final String REGISTRY_NAME = "diving.mask";
    public static final String MOD_CONTEXT = OceanicaInfo.MODID + ":"+ REGISTRY_NAME;

    public ItemMask() {
        super(MODID, REGISTRY_NAME,BaubleType.HEAD);
        MinecraftForge.EVENT_BUS.register(this);
    }

}
