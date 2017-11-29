package mc.oceanica.core;

import mc.oceanica.OceanicaInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.lang3.ArrayUtils;

import static mc.oceanica.core.BlockProperties.LEVEL;

/**
 *
 * must call super.createBlockState(properties)
 */
public abstract class AquaticBlock extends Block {

    public AquaticBlock(String registryName) {
        super(Material.WATER);

        setUnlocalizedName(OceanicaInfo.MODID + "."+ registryName);
        setRegistryName(OceanicaInfo.MODID, registryName);
        //TODO: play with this, or wait until 1.14 to see if there is a better way for aquatic blocks
        this.setDefaultState(this.getDefaultState().withProperty(LEVEL, 15));

    }

    public Item getItem() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }


    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) { return false; }

    /**
     *
     * @return a new BlockStateContainer with the properties of the subclass plus the LEVEL property
     */
    protected BlockStateContainer createBlockState(IProperty... properties) {
        return new BlockStateContainer(this,ArrayUtils.add(properties, LEVEL));
    }

    protected BlockStateContainer createBlockState() {
        return this.createBlockState(new IProperty[0]);
    }
}
