package com.loficostudios.christmasTreeDecor;

import com.loficostudios.christmasTreeDecor.Profile.ProfileAlreadyLoadedException;
import com.loficostudios.christmasTreeDecor.Profile.ProfileNotLoadedException;
import com.loficostudios.christmasTreeDecor.commands.OrnamentsCommand;
import com.loficostudios.christmasTreeDecor.file.impl.YamlFile;
import com.loficostudios.christmasTreeDecor.listeners.ChristmasTreeListener;
import com.loficostudios.christmasTreeDecor.listeners.PlayerListener;
import com.loficostudios.christmasTreeDecor.managers.ProfileManager;
import com.loficostudios.christmasTreeDecor.messages.Messages;
import com.loficostudios.christmasTreeDecor.utils.Debug;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class ChristmasTreeDecor extends JavaPlugin {

    public static StateFlag ALLOW_ORNAMENTS;

    public static final String PLUGIN_PREFIX = "christmastreedecor.";

    public static Permission ORNAMENT_PERMISSION = new Permission(PLUGIN_PREFIX + "decorator");

    @Getter
    private static ChristmasTreeDecor instance;

    private final ProfileManager profileManager = new ProfileManager();

    @Getter
    private final List<Location> globalOrnamentLocations = new ArrayList<>();

    @Getter
    private YamlFile messagesFile;
    @Getter
    private YamlFile configFile;

    @Override
    public void onLoad() {
        super.onLoad();

        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.getPlugin("WorldGuard") == null) {
            getLogger().log(Level.SEVERE, "Could not find WorldGuard in plugins folder. Disabling...");
            pluginManager.disablePlugin(this);
        }

        instance = this;
        loadConfigs();

        Messages.loadAllMessages();

        registerFlags();
    }

    @Override
    public void onEnable() {
        registerPermissions();
        registerListener();

        loadAllOrnaments();

        try {
            getCommand("ornaments").setExecutor(new OrnamentsCommand(this));
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Could not load command: " + "ornaments");
        }
    }

    @Override
    public void onDisable() {
    }

    public long reload() {
        long startTime = System.currentTimeMillis();

        loadConfigs();

        Messages.loadAllMessages();

        return System.currentTimeMillis() - startTime;
    }

    private void registerPermissions() {
        Bukkit.getPluginManager().addPermission(ORNAMENT_PERMISSION);
    }

    private void registerListener() {
        List.of(
                new ChristmasTreeListener(this, profileManager),
                new PlayerListener(profileManager)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void registerFlags() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StateFlag flag = new StateFlag("allow-ornaments", true);
            registry.register(flag);
            ALLOW_ORNAMENTS = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("allow-ornaments");
            if (existing instanceof StateFlag) {
                ALLOW_ORNAMENTS = (StateFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
            }
        }
    }

    private void loadConfigs() {
        this.messagesFile = new YamlFile("messages.yml", this);
        this.configFile = new YamlFile("config.yml", this);
    }

    public void loadAllOrnaments() {
        Logger logger = getLogger();

        File file = new File(getDataFolder().getPath() + "/players");
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles((dir, name) -> name.endsWith(".yml"));
            if (files != null) {
                for (File yaml : files) {
                    String uuid = yaml.getName().replaceFirst("[.][^.]+$", "");
                    YamlFile configFile = new YamlFile("players/" + uuid + ".yml", this);

                    List<Location> locationsFromFile = new ArrayList<>();
                    locationsFromFile = (List<Location>) configFile.getConfig().get("locations", locationsFromFile);

                    globalOrnamentLocations.addAll(locationsFromFile);
                }
            }
        }

        Debug.log("Loaded: " + globalOrnamentLocations.size() + " " + globalOrnamentLocations);
    }

    public long removeAllOrnament() {

        int amountRemoved = 0;

        for (Location loc : globalOrnamentLocations) {
            Block block = loc.getBlock();

            Material type = block.getType();

            if (type.equals(Material.PLAYER_HEAD) || type.equals(Material.PLAYER_WALL_HEAD)) {
                block.setType(Material.AIR);
                amountRemoved++;
            }
        }

        File file = new File(getDataFolder().getPath() + "/players");
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles((dir, name) -> name.endsWith(".yml"));
            if (files != null) {
                for (File yaml : files) {
                    UUID uuid = UUID.fromString(yaml.getName().replaceFirst("[.][^.]+$", ""));

                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    if (yaml.delete()) {

                        getLogger().log(Level.INFO, "Removed Ornaments for " + player.getName());
                        try {
                            profileManager.unloadProfile(uuid);
                        } catch (ProfileNotLoadedException ignored) {
                        }

                        if (player.isOnline()) {
                            try {
                                profileManager.loadProfile(uuid);
                            } catch (ProfileAlreadyLoadedException ignored) {
                            }
                        }
                    }
                }
            }
        }

        globalOrnamentLocations.clear();

        return amountRemoved;
    }
}
