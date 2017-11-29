package mc.oceanica.module.diving.gear;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import mc.oceanica.Oceanica;
import mc.oceanica.OceanicaConfig;
import mc.oceanica.OceanicaInfo;
import mc.oceanica.core.ItemBauble;
import mc.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static mc.oceanica.OceanicaInfo.MODID;

public class ItemBuoyancyBelt extends ItemBauble {
    public static final String REGISTRY_NAME = "diving.buoyancy.belt";
    public static final String MOD_CONTEXT = OceanicaInfo.MODID + ":" + REGISTRY_NAME;

    public ItemBuoyancyBelt() {
        super(MODID,REGISTRY_NAME, BaubleType.BELT);
    }


    //TODO: "fix" dive into the ocean
    //TODO: when damage is received, the movement stops
    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (!player.getEntityWorld().isRemote) return;
        if (EntityUtil.isInWater(player)) {
            if (player.isSneaking()) {
                player.motionY = OceanicaConfig.diving.buoyancyBeltDescendSpeed;
            } else if (player.motionY > 0 && player.motionY < OceanicaConfig.diving.buoyancyBeltAscendSpeed) {
                player.motionY = OceanicaConfig.diving.buoyancyBeltAscendSpeed;
            } else {
                player.motionY = 0;
            }
        }
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F, 1f);
    }

}
