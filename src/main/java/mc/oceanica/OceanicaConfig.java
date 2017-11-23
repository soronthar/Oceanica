package mc.oceanica;

import net.minecraftforge.common.config.Config;

@Config(modid = OceanicaInfo.MODID, category = "Oceanica")
public class OceanicaConfig {
    public static boolean isDebug=false;

    @Config.Name("Reef")
    public static final Reef reef=new Reef();

    public static class Reef {

        @Config.Comment("Configuration for Procedural Generation. Noise values go from -1 to 1.")
        public ProcGen procGen =new ProcGen();
        public static class ProcGen {

            @Config.Comment("Kelp will generate if the noise value of the chunk is less than this value.")
            @Config.RangeDouble(min = -1d, max = 1d)
            public double kelpChunkThreshold = -0.55;

            @Config.Comment("Primary seed chunk will generate if the noise value of the chunk is greather than this value.")
            @Config.RangeDouble(min = -1d, max = 1d)
            public double seedChunkThreshold = 0.1;

            @Config.Comment("Scale of the noise used to determine the chunk content")
            public int reefNoiseScale = 2;

            @Config.Comment("Inside a primary seed chunk, a coral will be generated if the noise of the block is greather than the this threshold.")
            @Config.RangeDouble(min = -1d, max = 1d)
            public double primaryReefDensityThreshold = 0.2;

            @Config.Comment("Inside a secondary reef chunk, a coral will be generated if the noise of the block is greather than the this threshold.")
            @Config.RangeDouble(min = -1d, max = 1d)
            public double secondaryReefDensityThreshold = 0.5;

            @Config.Comment("Inside a kelp chunk, a kelp will be generated if the noise of the block is greather than the this threshold.")
            @Config.RangeDouble(min = -1d, max = 1d)
            public double kelpDensityThreshold = -0.2;

            @Config.Comment("Coral density for coral chunks.")
            @Config.RangeDouble(min = -1d, max = 1d)
            public double coralDensity = 0.7d;
        }

        @Config.Comment("Minimum level that coral and kelp spawn")
        public int minLevel = 5;

        @Config.Comment("Offset under sea level where the coral spawn. I.e, if Sea Level=64 and maxLevelOffset=5, corals will spawn on y=59 and below")
        public int maxLevelOffset = 5;

        @Config.Comment("Enable Coral Reef generation")
        public boolean generateReef=true;
    }

    @Config.Name("Abyss")
    public static final Abyss abyss=new Abyss();

    public static class Abyss {

        @Config.Comment("Block used as Ocean Floor. Currently only used by the trenches")
        public String oceanFloorBlock="minecraft:sand";

        @Config.Name("Trenches")
        public final Trenches trenches=new Trenches();

        public static class Trenches {

            @Config.Comment("Enable Ocean Trenches")
            public boolean enableTrenches=true;

            @Config.Comment("Rarity of the trenches. Higher means less frequent trenches")
            public int trenchRarity=100;
            @Config.Comment("Length of the trenches")
            public int trenchLength =12;
            @Config.Comment("Median width of the trenches. The width of each layer will be between trenchWidth/4 and trenchWidth * 2")
            public float trenchWidth=5f;
            @Config.Comment("Depth of the trenches.")
            public double trenchDepth=3.5d;

        }

    }

    @Config.Name("Diving")
    public static final Diving diving=new Diving();

    public static class Diving {

        @Config.Comment("Depth under sea level where the snorkel works.")
        public int snorkelDepth = 6;
    }
}
