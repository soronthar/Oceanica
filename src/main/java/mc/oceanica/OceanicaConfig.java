package mc.oceanica;

import net.minecraftforge.common.config.Config;

@Config(modid = OceanicaInfo.MODID)
public class OceanicaConfig {
    public static boolean isDebug=false;

    @Config.Comment("Reef Generation configuration.")
    public static final Reef reef=new Reef();

    public static class Reef {
        public double baseDecreaseFactor =1.25d;
        public double seedChunkFrequency =0.012d;
        public double reefStoneDensity =0.55d;
        public double densityDecrease = 0.25d;
        public boolean enableSecondarySeedChunk=false;
        public double coralDensity = 0.7d;
        public  boolean generateReef=true;
    }


    @Config.Comment("Abyss configuration")
    public static final Abyss abyss=new Abyss();

    public static class Abyss {

        @Config.Comment("Block used as Ocean Floor. Currently only used by the trenches")
        public String oceanFloorBlock="minecraft:sand";
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

    //    public static void readConfig(File file, boolean caseSensitiveCustomCategories) {
//        config = new Configuration(file,caseSensitiveCustomCategories);
//
//        try {
//            config.load();
//            generateReef = config.get(CATEGORY_REEF.label, "generateReef", generateReef, "Generate Coral Reef chunks").getBoolean();
//            seedChunkFrequency = config.get(CATEGORY_REEF.label, "seedChunkFrequency", seedChunkFrequency, "Coral Reef seed chunk frequency. Higher means more frequent (0-1)", 0, 1).getDouble();
//            reefStoneDensity = config.get(CATEGORY_REEF.label, "reefStoneDensity", reefStoneDensity, "Reef stone density on the main seed chunk. Higher means more coral stone in the chunk (0-1)").getDouble();
//            densityDecrease = config.get(CATEGORY_REEF.label, "densityDecrease", densityDecrease, "Reef stone density decrease for secondary seed chunk. Higher means more coral stone in the chunk(0-1)").getDouble();
//            baseDecreaseFactor = config.get(CATEGORY_REEF.label, "baseDecreaseFactor", baseDecreaseFactor, "Reef stone density decrease for secondary seed chunk. Secondary chunk density is reefStoneDensity - ((baseDecreaseFactor + rand()) * densityDecrease)").getDouble();
//            coralDensity = config.get(CATEGORY_REEF.label, "coralDensity", coralDensity, "The chance a Coral will spawn on top of a reef stone. Higher means more frequent (0-1)").getDouble();
//            enableSecondarySeedChunk = config.get(CATEGORY_REEF.label, "enableSecondarySeedChunk", enableSecondarySeedChunk, "When enabled, a 3x3 area around the seed chunk will be also seeded with reduced density").getBoolean();
//            isDebug = config.get(CATEGORY_GENERAL.label, "debug", isDebug, "Set Debug Mode. Do not enable for normal gameplay").getBoolean();
//
//        } catch (Exception e1) {
//            Oceanica.logger.error("Problem loading config file!", e1);
//        } finally {
//            if (config.hasChanged()) {
//                config.save();
//            }
//        }
//    }



}
