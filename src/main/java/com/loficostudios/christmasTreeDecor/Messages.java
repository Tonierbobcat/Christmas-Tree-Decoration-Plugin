package com.loficostudios.christmasTreeDecor;

import com.loficostudios.christmasTreeDecor.file.impl.YamlFile;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

@UtilityClass
public class Messages {
    private final FileConfiguration messages = ChristmasTreeDecor.getInstance().getMessagesFile().getConfig();

    public static final String ORNAMENT_PLACED = getMessage("ORNAMENT_PLACED");
    public static final String NO_ORNAMENT = getMessage("NO_ORNAMENT");
    public static final String CANNOT_PLACE_ORNAMENT = getMessage("CANNOT_PLACE_ORNAMENT");
    public static final String PERMISSION_DENIED = getMessage("PERMISSION_DENIED");
    //public static final String NOT_IN_REGION = getMessage("NOT_IN_REGION");

    private static String getMessage(String path) {
        String z = "messages." + path;
        try {
            return messages.getString(z);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not load message at: " + "messages." + path + " " + e.getMessage());
        }
    }
}
