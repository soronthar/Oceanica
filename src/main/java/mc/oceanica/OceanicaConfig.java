package mc.oceanica;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

import static mc.oceanica.OceanicaConfig.ConfigCategories.CATEGORY_ABYSS;
import static mc.oceanica.OceanicaConfig.ConfigCategories.CATEGORY_GENERAL;
import static mc.oceanica.OceanicaConfig.ConfigCategories.CATEGORY_REEF;

public class OceanicaConfig {
    private static Configuration config;

    public static boolean isDebug=false;

    //Reef
    public static double baseDecreaseFactor =1.25d;
    public static double seedChunkFrequency =0.012d;
    public static double reefStoneDensity =0.55d;
    public static double densityDecrease = 0.25d;
    public static double coralDensity = 0.7d;

    //Abyss
    public static boolean enableTrenches=true;
    public static int trenchLength =12;
    public static int trenchRarity=10;
    public static float trenchWidth=5f;
    public static float trenchWidthLower=trenchWidth/4f;
    public static float trenchWidthUpper=trenchWidth*2f;
    public static double trenchDepth=3.5d;
    public static String oceanFloorBlock="minecraft:sand";

    public static void readConfig(File file, boolean caseSensitiveCustomCategories) {
        config = new Configuration(file,caseSensitiveCustomCategories);

        try {
            config.load();
            seedChunkFrequency = config.get(CATEGORY_REEF.label, "seedChunkFrequency", seedChunkFrequency, "Coral Reef seed chunk frequency. Higher means more frequent (0-1)", 0, 1).getDouble();
            reefStoneDensity = config.get(CATEGORY_REEF.label, "reefStoneDensity", reefStoneDensity, "Reef stone density on the main seed chunk. Higher means more coral stone in the chunk (0-1)").getDouble();
            densityDecrease = config.get(CATEGORY_REEF.label, "densityDecrease", densityDecrease, "Reef stone density decrease for secondary seed chunk. Higher means more coral stone in the chunk(0-1)").getDouble();
            baseDecreaseFactor = config.get(CATEGORY_REEF.label, "baseDecreaseFactor", baseDecreaseFactor, "Reef stone density decrease for secondary seed chunk. Secondary chunk density is reefStoneDensity - ((baseDecreaseFactor + rand()) * densityDecrease)").getDouble();
            coralDensity = config.get(CATEGORY_REEF.label, "coralDensity", coralDensity, "The chance a Coral will spawn on top of a reef stone. Higher means more frequent (0-1)").getDouble();

            enableTrenches =config.get(CATEGORY_ABYSS.label, "enableTrenches", enableTrenches, "Enable Ocean Trenches").getBoolean();
            trenchRarity = config.get(CATEGORY_ABYSS.label, "trenchRarity", trenchRarity, "Rarity of the trenches. Higher means rarer trenches").getInt();
            trenchLength = config.get(CATEGORY_ABYSS.label, "trenchLength", trenchLength, "Length of the trenches.").getInt();
            trenchWidth = (float) config.get(CATEGORY_ABYSS.label, "trenchWidth", trenchWidth, "Median width of the trenches. The width of each layer will be between trenchWidth/4 and trenchWidth * 2").getDouble();
            trenchWidthLower=trenchWidth/4f;
            trenchWidthUpper=trenchWidth*2f;

            trenchDepth = config.get(CATEGORY_ABYSS.label, "trenchDepth", trenchDepth, "Depth of the trenches.").getDouble();
            oceanFloorBlock = config.get(CATEGORY_ABYSS.label, "oceanFloorBlock", oceanFloorBlock, "Block to be used as the ocean floor.").getString();

            isDebug = config.get(CATEGORY_GENERAL.label, "debug", isDebug, "Set Debug Mode. Do not enable for normal gameplay").getBoolean();

        } catch (Exception e1) {
            Oceanica.logger.error("Problem loading config file!", e1);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    private static void fillUpDefaults(Configuration cfg) {
addCategories(cfg);
    }

    private static void addCategories(Configuration cfg) {
        ConfigCategories[] values = ConfigCategories.values();
        for (int i = 0; i < values.length; i++) {
            ConfigCategories value = values[i];
            cfg.addCustomCategoryComment(value.label,value.comment );
        }


    }

    public static void save() {
        if (config.hasChanged()) {
            config.save();
        }
    }


    static enum ConfigCategories {
        CATEGORY_ABYSS("Abyss","Abyss configuration"),
        CATEGORY_DIVING("Diving", "Diving configuration"),
        CATEGORY_REEF("Reef", "Reef configuration"),
        CATEGORY_GENERAL("General", "General configuration");


        String label;
        String comment;

        ConfigCategories(String label, String comment) {
            this.label = label;
            this.comment=comment;
        }

    }

}
