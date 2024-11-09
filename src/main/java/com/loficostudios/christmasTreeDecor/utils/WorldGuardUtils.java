package com.loficostudios.christmasTreeDecor.utils;

import com.loficostudios.christmasTreeDecor.ChristmasTreeDecor;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.sk89q.worldguard.protection.flags.Flags;
import java.util.Collection;

@UtilityClass
public class WorldGuardUtils {
    public boolean isPlayerInRegionWithFlag(Player player) {
        Location playerLocation = player.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(player.getWorld()));

        if (regionManager == null) return false;

        ApplicableRegionSet regions = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(playerLocation));

        // Iterate through each region to check for the flag
        for (ProtectedRegion region : regions) {
            if (region.getFlag(ChristmasTreeDecor.ALLOW_ORNAMENTS) == StateFlag.State.ALLOW) {
                return true; // Player is in a region with the specified flag set to ALLOW
            }
        }
        return false; // No region with the specified flag found
    }
}