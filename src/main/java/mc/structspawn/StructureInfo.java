package mc.structspawn;

import net.minecraft.util.ResourceLocation;

public class StructureInfo {
    private String name;
    private BlockPalette palette;
    private ResourceLocation resourceLocation;

    public StructureInfo(String name, ResourceLocation resourceLocation) {
        this(name, resourceLocation, BlockPalette.DEFAULT);
    }

    public StructureInfo(String name, ResourceLocation resourceLocation, BlockPalette palette) {
        this.resourceLocation = resourceLocation;
        this.palette = palette;
        this.name = name;
    }

    public StructureInfo(ResourceLocation resourceLocation) {
        this(resourceLocation.getResourcePath(),resourceLocation,BlockPalette.DEFAULT);
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public BlockPalette getPalette() {
        return palette;
    }

    public String getName() {
        return name;
    }
}
