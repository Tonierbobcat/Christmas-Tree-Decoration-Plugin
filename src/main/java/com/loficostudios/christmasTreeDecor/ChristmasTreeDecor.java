package com.loficostudios.christmasTreeDecor;

import com.loficostudios.christmasTreeDecor.file.impl.YamlFile;
import com.loficostudios.christmasTreeDecor.listeners.ChristmasTreeListener;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

public final class ChristmasTreeDecor extends JavaPlugin {
    @Getter
    private YamlFile messagesFile;
    @Getter
    private static ChristmasTreeDecor instance;

    public static StateFlag ALLOW_ORNAMENTS;

    public static final String PLUGIN_PREFIX = "christmas_tree.";

    public static Permission ORNAMENT_PERMISSION = new Permission(PLUGIN_PREFIX + "decorator");

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
        registerFlags();
    }

    @Override
    public void onEnable() {
        registerPermissions();
        registerListener();
    }

    @Override
    public void onDisable() {
    }

    private void loadConfigs() {
        this.messagesFile = new YamlFile("messages.yml", this);
    }

    private void registerPermissions() {
        Bukkit.getPluginManager().addPermission(ORNAMENT_PERMISSION);
    }

    private void registerListener() {
        List.of(
                new ChristmasTreeListener(this)
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
}
