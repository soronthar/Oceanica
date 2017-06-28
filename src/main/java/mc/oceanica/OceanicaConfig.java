package mc.oceanica;

import mc.oceanica.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

import static mc.oceanica.OceanicaConfig.ConfigCategories.CATEGORY_ABYSS;

public class OceanicaConfig {


    public static void readConfig() {
        Configuration cfg = CommonProxy.config;

        try {
            cfg.load();
            if (!cfg.hasCategory(CATEGORY_ABYSS.label)) {
                addCategories(cfg);
            }


        } catch (Exception e1) {
            Oceanica.logger.error("Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void addCategories(Configuration cfg) {
        ConfigCategories[] values = ConfigCategories.values();
        for (int i = 0; i < values.length; i++) {
            ConfigCategories value = values[i];
            cfg.addCustomCategoryComment(value.label,value.comment );

        }
    }

    static enum ConfigCategories {
        CATEGORY_ABYSS("Abyss","Abyss configuration"),
        CATEGORY_DIVING("Diving", "Diving configuration"),
        CATEGORY_REEF("Reef", "Reef configuration");


        String label;
        String comment;

        ConfigCategories(String label, String comment) {
            this.label = label;
            this.comment=comment;
        }

    }

}
