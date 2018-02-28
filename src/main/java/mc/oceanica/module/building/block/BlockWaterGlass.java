package mc.oceanica.module.building.block;

import mc.oceanica.OceanicaInfo;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static mc.oceanica.core.BlockProperties.LEVEL;

//TODO: make the item look like the actual glass
public class BlockWaterGlass extends BlockHorizontal {
    public static final String REGISTRY_NAME = "build.waterglass";
    public static final String MOD_CONTEXT =  OceanicaInfo.MODID + ":"+ REGISTRY_NAME;

    public BlockWaterGlass() {
        super(Material.WATER);
        setUnlocalizedName(OceanicaInfo.MODID + "."+ REGISTRY_NAME);
        setRegistryName(OceanicaInfo.MODID, REGISTRY_NAME);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LEVEL, 15));

    }

    public Item getItem() { return new ItemBlock(this).setRegistryName(this.getRegistryName()); }


    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING,LEVEL);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return blockAccess.getBlockState(pos.offset(side)).getBlock() != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    protected static final   AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] {
            new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D,  1.0D), //South
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D,  1.0D),    //West
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D,  0.0625D),    //North
            new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D,  1.0D)     //East
    };

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = this.getActualState(state, source, pos);
        return AABB_BY_INDEX[state.getValue(FACING).getHorizontalIndex()];
    }
}
