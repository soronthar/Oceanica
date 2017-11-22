package mc.oceanica.module.diving.gear;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import mc.oceanica.OceanicaInfo;
import mc.oceanica.module.diving.DivingModule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static mc.oceanica.OceanicaInfo.MODID;

//TODO: have two different masks, one with Snorkel and one without it
@Mod.EventBusSubscriber
public class ItemMask extends Item implements IBauble{
    public static final String REGISTRY_NAME = "diving.mask";
    public static final String MOD_CONTEXT = OceanicaInfo.MODID + ":"+ REGISTRY_NAME;

    public ItemMask() {
        this.setUnlocalizedName(OceanicaInfo.MODID + "."+ REGISTRY_NAME);
        this.setRegistryName(new ResourceLocation(MODID, REGISTRY_NAME));
        this.setMaxStackSize(1);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.HEAD;
    }

    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (isInWater(player)) {
            if (player.getEntityWorld().getSeaLevel() - player.getPosition().getY() < 6 && player.getAir() < 300) {
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
        boolean equals = hasBaubleInSlot(player, baubleType, item);
        if (event.getEntity() == player && equals) {
            if (isInWater(player)) {
                event.setDensity(0F);
                event.setCanceled(true);
            }
        }
    }

    private boolean hasBaubleInSlot(EntityPlayer player, BaubleType baubleType, ItemMask item) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
        int[] validSlots = baubleType.getValidSlots();
        for (int validSlot : validSlots) {
            if (baubles.getStackInSlot(validSlot).getItem().equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if the entity is covered in enough water
     *
     * @param entity the entity
     * @return if it's considered as in water
     */
    public static boolean isInWater(Entity entity) {
        if (entity.isInsideOfMaterial(Material.WATER)) return true;

        double eyes = entity.posY + (double) entity.getEyeHeight() - 0.65;
        int i = MathHelper.floor(entity.posX);
        int j = MathHelper.floor(MathHelper.floor(eyes));
        int k = MathHelper.floor(entity.posZ);

        BlockPos pos = new BlockPos(i, j, k);
        IBlockState state = entity.getEntityWorld().getBlockState(pos);
        Block block = state.getBlock();

        if (state.getMaterial() == Material.WATER) {
            double filled = 1.0f; //If it's not a liquid assume it's a solid blocks
            if (block instanceof IFluidBlock) {
                filled = ((IFluidBlock) block).getFilledPercentage(entity.getEntityWorld(), pos);
            } else if (block instanceof BlockLiquid) {
                filled = BlockLiquid.getLiquidHeightPercent(block.getMetaFromState(state));
            }

            if (filled < 0) {
                filled *= -1;
                return eyes > pos.getY() + 1 + (1 - filled);
            } else {
                return eyes < pos.getY() + 1 + filled;
            }
        } else return false;
    }

}
