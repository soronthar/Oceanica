package mc.structgen;

import net.minecraft.util.ResourceLocation;

/**
 * Created by H440 on 31/10/2017.
 */
public class StructureInfo {
    private BlockPalette palette;
    private ResourceLocation resourceLocation;

    public StructureInfo(ResourceLocation resourceLocation) {
        this.resourceLocation=resourceLocation;
        this.palette=BlockPalette.DEFAULT;
    }

    public StructureInfo(ResourceLocation resourceLocation, BlockPalette palette) {
        this.resourceLocation=resourceLocation;
        this.palette=palette;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public BlockPalette getPalette() {
        return palette;
    }
}
