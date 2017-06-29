package mc.oceanica.module.reef.block;

import mc.oceanica.core.AquaticBlock;
import mc.oceanica.core.BlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockCoral extends AquaticBlock implements IPlantable {
    //TODO drop if the supporting block is destroyed
    public static final EnumPlantType CORAL = EnumPlantType.getPlantType("Coral");
    //    public static final PropertyInteger VARIANTS = PropertyInteger.create("variants", 0, 15);


    protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.6000000238418579D, 0.699999988079071D);


    public BlockCoral() {
        super("reef.coral");

        setTickRandomly(false);
        setCreativeTab(CreativeTabs.MISC);
        setHardness(0.0F);
        setSoundType(SoundType.PLANT);
        setLightLevel(0.5F);

    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BlockProperties.LEVEL, meta);
    }

    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(BlockProperties.LEVEL);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BUSH_AABB;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }


    //------
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos.down());
        Block block = state.getBlock();

        if (worldIn.getBlockState(pos.up(1)).getMaterial() != Material.WATER) return false;
        if (block.canSustainPlant(state, worldIn, pos.down(), EnumFacing.UP, this)) return true;
//        if (block == this) {
//            int variant = state.getValue(VARIANTS);
//            return variant == 13;
//        }
        return false;
    }


    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return CORAL;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos);
    }

}
