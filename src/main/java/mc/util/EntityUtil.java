package mc.util;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mc.oceanica.module.diving.gear.ItemMask;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.IFluidBlock;

public class EntityUtil {

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

    public static boolean hasBaubleInSlot(EntityPlayer player, BaubleType baubleType, ItemMask item) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
        int[] validSlots = baubleType.getValidSlots();
        for (int validSlot : validSlots) {
            if (baubles.getStackInSlot(validSlot).getItem().equals(item)) {
                return true;
            }
        }
        return false;
    }


    public static boolean hasArmor(EntityPlayer player, EntityEquipmentSlot slot, Item item) {
        ItemStack stack = player.getItemStackFromSlot(slot);
        return !stack.isEmpty() && stack.getItem() == item;
    }
}
