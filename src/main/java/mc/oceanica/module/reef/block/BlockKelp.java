package mc.oceanica.module.reef.block;

import mc.oceanica.OceanicaInfo;
import mc.oceanica.core.AquaticBlock;
import mc.oceanica.module.reef.ReefModule;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockKelp extends AquaticBlock implements net.minecraftforge.common.IPlantable {
    public static final String REGISTRY_NAME = "reef.kelp";
    public static final String MOD_CONTEXT = OceanicaInfo.MODID + ":" + REGISTRY_NAME;

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    protected static final AxisAlignedBB REED_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);


    public BlockKelp() {
        super(REGISTRY_NAME);
        this.setTickRandomly(true);
        setCreativeTab(CreativeTabs.MISC);
        setHardness(0.0F);
        setSoundType(SoundType.PLANT);
        setLightLevel(0.25F);
        this.setDefaultState(getDefaultState().withProperty(AGE, 0));
    }


    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
//        if (worldIn.getBlockState(pos.down()).getBlock() == ReefModule.KELP || this.checkForDrop(worldIn, pos, state)) {
//            if (worldIn.isAirBlock(pos.up())) {
//                int i;
//
//                for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; ++i) {
//                    ;
//                }
//
//                if (i < 3) {
//                    int j = ((Integer) state.getValue(AGE)).intValue();
//
//                    if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, true)) {
//                        if (j == 15) {
//                            worldIn.setBlockState(pos.up(), this.getDefaultState());
//                            worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(0)), 4);
//                        } else {
//                            worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(j + 1)), 4);
//                        }
//                        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
//                    }
//                }
//            }
//        }
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return REED_AABB;
    }


    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        IBlockState baseBlock = world.getBlockState(pos.down());
        IBlockState targetBlock = world.getBlockState(pos);

        return isWaterBlock(targetBlock)
                && canSustainKelp(baseBlock)
                && pos.getY() < (world.getSeaLevel() - 1);
    }

    private boolean canSustainKelp(IBlockState baseBlock) {
        return baseBlock.getBlock() == Blocks.SAND || baseBlock.getBlock() == Blocks.GRAVEL|| baseBlock.getBlock() == Blocks.DIRT|| baseBlock.getBlock() == this;
    }

    private boolean isWaterBlock(IBlockState baseBlock) {
        return baseBlock.getBlock() == Blocks.WATER || baseBlock.getBlock() == Blocks.FLOWING_WATER;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canSustainKelp(worldIn.getBlockState(pos.down()))) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
        }

        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (this.canBlockStay(worldIn, pos)) {
            return true;
        } else {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        return this.canPlaceBlockAt(worldIn, pos);
    }

    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ReefModule.KELP_ITEM;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(ReefModule.KELP);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((Integer) state.getValue(AGE)).intValue();
    }


    protected BlockStateContainer createBlockState() {
        return super.createBlockState(AGE);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Water;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return this.getDefaultState();
    }
}
