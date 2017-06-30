package mc.oceanica.module.diving.gear;

import mc.oceanica.OceanicaInfo;
import mc.oceanica.module.diving.DivingModule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static mc.oceanica.OceanicaInfo.MODID;

@Mod.EventBusSubscriber
public class ItemMask extends ItemArmor {
    public static final String REGISTRY_NAME = "diving.mask";
    public static final String MOD_CONTEXT = OceanicaInfo.MODID + ":"+ REGISTRY_NAME;

    //TODO: head model
    public ItemMask() {
        super(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD);
        this.setUnlocalizedName(OceanicaInfo.MODID + "."+ REGISTRY_NAME);
        this.setRegistryName(new ResourceLocation(MODID, REGISTRY_NAME));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getSlot() == EntityEquipmentSlot.HEAD) {
            EntityLivingBase player = event.getEntityLiving();
            if (event.getTo().getItem().equals(DivingModule.ITEM_MASK)) {
                PotionEffect effect = player.getActivePotionEffect(MobEffects.WATER_BREATHING);
                if (effect == null) {
                    PotionEffect neweffect = new PotionEffect(MobEffects.WATER_BREATHING, Integer.MAX_VALUE, -42, true, false);
                    player.addPotionEffect(neweffect);
                }
            } else if (event.getFrom().getItem().equals(DivingModule.ITEM_MASK)) {
                PotionEffect effect = player.getActivePotionEffect(MobEffects.WATER_BREATHING);
                if (effect != null) {
                    player.removePotionEffect(MobEffects.WATER_BREATHING);
                }
            }

        }
    }

    /**
     * Returns if the entity is covered in enough water
     *
     * @param entity the entity
     * @return if it's considered as in water
     */
    public static boolean isInWater(Entity entity) {
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

    public static boolean hasArmor(EntityPlayer player, EntityEquipmentSlot slot, Item item) {
        ItemStack stack = player.getItemStackFromSlot(slot);
        return !stack.isEmpty() && stack.getItem() == item;
    }

}
