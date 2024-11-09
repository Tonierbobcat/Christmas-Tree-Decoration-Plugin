package com.loficostudios.christmasTreeDecor.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

import java.util.logging.Level;

@UtilityClass
public class Debug {

    private static final boolean DEBUGS_ENABLED = false;

    public static void log(String message) {
        if (!DEBUGS_ENABLED)
            return;
        Bukkit.getLogger().log(Level.INFO, message);
    }
}
