package mc.oceanica.core;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import mc.oceanica.OceanicaInfo;
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


//TODO: change this to use capabilities instead of IBauble, for the fun of it.
public abstract  class ItemBauble extends Item implements IBauble {
    private BaubleType baubleType;

    public ItemBauble(String modId, String registryName, BaubleType baubleType) {
        this.setRegistryName(new ResourceLocation(modId,registryName));
        this.setUnlocalizedName(modId + "." + registryName);
        this.setMaxStackSize(1);
        this.baubleType=baubleType;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
        for (int i = 0; i < baubles.getSlots(); i++)
            if ((baubles.getStackInSlot(i).isEmpty()) && baubles.isItemValidForSlot(i, player.getHeldItem(hand), player)) {
                baubles.setStackInSlot(i, player.getHeldItem(hand).copy());
                if (!player.capabilities.isCreativeMode) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                }
                onEquipped(player.getHeldItem(hand), player);
                break;
            }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1F, 1f);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return this.baubleType;
    }
}
