package com.loficostudios.christmasTreeDecor.listeners;

import com.loficostudios.christmasTreeDecor.Profile.ProfileAlreadyLoadedException;
import com.loficostudios.christmasTreeDecor.Profile.ProfileNotLoadedException;
import com.loficostudios.christmasTreeDecor.managers.ProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    final ProfileManager profileManager;

    public PlayerListener(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @EventHandler
    private void onJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        try {
            profileManager.loadProfile(player.getUniqueId());
        } catch (ProfileAlreadyLoadedException exception) {
        }
    }

    @EventHandler
    private void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();

        try {
            profileManager.unloadProfile(player.getUniqueId());
        } catch (ProfileNotLoadedException exception) {
        }
    }
}
