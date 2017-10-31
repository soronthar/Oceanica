package mc.structgen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import java.util.HashMap;
import java.util.Map;


public class BlockPalette {
    public static final BlockPalette DEFAULT = new BlockPalette() {
        @Override
        public void addTransform(IBlockState original, IBlockState replacement) {
            throw new UnsupportedOperationException("Cannot add Transforms to the default BlockPalette");
        }
    };

    private Map<IBlockState,IBlockState> transforms=new HashMap<>();

    public void addTransform(IBlockState original, IBlockState replacement) {
        transforms.put(original,replacement);
    }

    public IBlockState transform(IBlockState original) {
        return transforms.getOrDefault(original,original);
    }
}
