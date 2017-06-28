package mc.oceanica.module.reef.block;

import mc.oceanica.Oceanica;
import mc.oceanica.OceanicaInfo;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class BlockReefStone  extends Block {

    public BlockReefStone() {
        super(Material.ROCK);
        setCreativeTab(CreativeTabs.MISC); //TODO: creative tab for each Oceanica module
        setHardness(1.5F);
        setResistance(10.0F);
        setSoundType(SoundType.STONE);
        setUnlocalizedName(OceanicaInfo.MODID+".reef.reefstone");
        setRegistryName(OceanicaInfo.MODID,"reef.reefstone");
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
    }


    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return plantable.getPlantType(world, pos.offset(direction)) == BlockCoral.CORAL;
    }

}
