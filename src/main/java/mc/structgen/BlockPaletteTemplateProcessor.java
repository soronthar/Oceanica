package mc.structgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.Template;

import javax.annotation.Nullable;

class BlockPaletteTemplateProcessor implements ITemplateProcessor {
    BlockPalette palette;

    public BlockPaletteTemplateProcessor(BlockPalette palette) {
        this.palette = palette;
    }


    @Nullable
    @Override
    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
        IBlockState newBlockState;
//            if (blockInfoIn.blockState.getBlock().equals(Blocks.AIR)) { //Make this block configurable
//                newBlockState = worldIn.getBlockState(pos);
//            } else {
        newBlockState = palette.transform(blockInfoIn.blockState);
//            }
        return new Template.BlockInfo(blockInfoIn.pos, newBlockState, blockInfoIn.tileentityData);
    }
}
