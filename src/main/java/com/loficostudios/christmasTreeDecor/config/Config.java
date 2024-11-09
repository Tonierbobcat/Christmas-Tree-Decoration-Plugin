package com.loficostudios.christmasTreeDecor.config;

import com.loficostudios.christmasTreeDecor.ChristmasTreeDecor;

import java.util.logging.Level;

public class Config {

    public static final int MAX_ORNAMENTS_PLAYER_CAN_PLACE = getMaxOrnamentsFromConfig();

    private static int getMaxOrnamentsFromConfig() {
        int max = 1;

        try {
            max = ChristmasTreeDecor.getInstance().getConfigFile().getInt("max-ornaments");
        } catch (Exception e) {
            ChristmasTreeDecor.getInstance().getLogger().log(Level.SEVERE, "Could not find path: max-ornaments");
        }

        return max;
    }
}
