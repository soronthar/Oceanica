package mc.oceanica.module.reef.block;

import mc.oceanica.OceanicaInfo;
import mc.oceanica.core.AquaticBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;


public class BlockCoral extends AquaticBlock implements IPlantable {
    public static final String REGISTRY_NAME = "reef.coral";
    public static final String MOD_CONTEXT =  OceanicaInfo.MODID + ":"+ REGISTRY_NAME;

    static final EnumPlantType CORAL = EnumPlantType.getPlantType("Coral");
    public static final PropertyEnum<EnumDyeColor> CORAL_TYPE = PropertyEnum.create("coraltype", EnumDyeColor.class);


    protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.6000000238418579D, 0.699999988079071D);


    public BlockCoral() {
        super("reef.coral");

        setTickRandomly(false);
        setCreativeTab(CreativeTabs.MISC);
        setHardness(0.0F);
        setSoundType(SoundType.PLANT);
        setLightLevel(0.5F);
        this.setDefaultState(this.getDefaultState().withProperty(CORAL_TYPE, EnumDyeColor.BLACK));
    }

    @Override
    public Item getItem() {
        ItemBlock item=new ItemBlock(this) {
            @Override
            public int getMetadata(int damage) {
                return damage;
            }
        };
        item.setUnlocalizedName(this.getUnlocalizedName());
        item.setRegistryName(getRegistryName());
        item.setHasSubtypes(true);
       return item;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(CORAL_TYPE).getMetadata();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(CORAL_TYPE,EnumDyeColor.byMetadata(meta));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(CORAL_TYPE).getMetadata());
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab.equals(getCreativeTabToDisplayOn())) {
            for (EnumDyeColor dyeColor : EnumDyeColor.values()) {
                items.add(new ItemStack(this, 1, dyeColor.getMetadata()));
            }
        }
    }

    protected BlockStateContainer createBlockState() {
        return super.createBlockState(CORAL_TYPE);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(CORAL_TYPE, EnumDyeColor.byMetadata(meta));
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(CORAL_TYPE).getMetadata();
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
//            int variant = state.getValue(CORAL_TYPE);
//            return variant == 13;
//        }
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
        }

        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(CORAL_TYPE,EnumDyeColor.byMetadata(meta));
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
