package mc.oceanica.module.diving.gear;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import mc.oceanica.OceanicaInfo;
import mc.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static mc.oceanica.OceanicaInfo.MODID;

public class ItemBuoyancyBelt extends Item implements IBauble {
    public static final String REGISTRY_NAME = "diving.buoyancy.belt";
    public static final String MOD_CONTEXT = OceanicaInfo.MODID + ":"+ REGISTRY_NAME;

    public ItemBuoyancyBelt() {
        this.setUnlocalizedName(OceanicaInfo.MODID + "."+ REGISTRY_NAME);
        this.setRegistryName(new ResourceLocation(MODID, REGISTRY_NAME));
        this.setMaxStackSize(1);
    }


    //TODO: "fix" dive into the ocean
    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (!player.getEntityWorld().isRemote) return;
        if (EntityUtil.isInWater(player)) {
            if (player.isSneaking()) {
                player.motionY=-0.1d;
            } else {
                player.motionY=0;
            }
        }
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.BELT;
    }
}
