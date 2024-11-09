package com.loficostudios.christmasTreeDecor.messages;

import com.loficostudios.christmasTreeDecor.ChristmasTreeDecor;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;


@UtilityClass
public class Messages {

    private final FileConfiguration messages = ChristmasTreeDecor.getInstance().getMessagesFile().getConfig();


    public static String CHAT_PREFIX;

    public static String ORNAMENT_PLACED;
    public static String NO_ORNAMENT;
    public static String CANNOT_PLACE_ORNAMENT;
    public static String PERMISSION_DENIED;
    public static String MAX_ORNAMENTS;

    public static String ORNAMENT_REMOVE_DENY;
    public static String ORNAMENT_REMOVE_ALLOW;

    //public static final String NOT_IN_REGION = getMessage("NOT_IN_REGION");

    public static void loadAllMessages() {

        CHAT_PREFIX = ChristmasTreeDecor.getInstance().getConfigFile().getConfig().getString("prefix");

        ORNAMENT_PLACED = getMessage("ORNAMENT_PLACED");
        NO_ORNAMENT = getMessage("NO_ORNAMENT");
        CANNOT_PLACE_ORNAMENT = getMessage("CANNOT_PLACE_ORNAMENT");
        PERMISSION_DENIED = getMessage("PERMISSION_DENIED");
        MAX_ORNAMENTS = getMessage("MAX_ORNAMENTS");

        ORNAMENT_REMOVE_DENY = getMessage("ORNAMENT_REMOVE_DENY");
        ORNAMENT_REMOVE_ALLOW = getMessage("ORNAMENT_REMOVE_ALLOW");
    }

    private static String getMessage(String path) {
        String z = "messages." + path;





        try {
            String message = messages.getString(z);

            if (Objects.requireNonNull(message).isEmpty() || message.equals(" "))
                return "";

            return CHAT_PREFIX + message;
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not load message at: " + "messages." + path + " " + e.getMessage());
        }
    }
}
