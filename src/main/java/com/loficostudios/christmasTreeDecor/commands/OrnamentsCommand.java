package com.loficostudios.christmasTreeDecor.commands;

import com.loficostudios.christmasTreeDecor.ChristmasTreeDecor;
import com.loficostudios.christmasTreeDecor.utils.ColorUtil;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class OrnamentsCommand implements CommandExecutor, TabExecutor {


    private final ChristmasTreeDecor plugin;

    public OrnamentsCommand(ChristmasTreeDecor plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 1) {

            if (strings[0].equalsIgnoreCase("removeall")) {
                long amountRemoved = plugin.removeAllOrnament();

                if (commandSender instanceof ConsoleCommandSender) {
                    plugin.getLogger().log(Level.INFO, "Removed " + amountRemoved +" ornament(s). Have a happy new year!");
                }
                else {
                    commandSender.sendMessage(ColorUtil.deserialize(
                            "&aRemoved &f" + amountRemoved + "&a ornament(s). Have a happy new year!"));
                }
                return true;
            }

            if (strings[0].equalsIgnoreCase("reload")) {


                long time = plugin.reload();

                commandSender.sendMessage(ColorUtil.deserialize(
                        "&aReloaded configs " + time + "ms"));

                return true;
            }
        }

        return false;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 1)
            return List.of("removeall", "reload");

        return new ArrayList<>();
    }
}
