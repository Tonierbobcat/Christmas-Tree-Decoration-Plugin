package com.loficostudios.christmasTreeDecor.utils;

import com.loficostudios.christmasTreeDecor.ChristmasTreeDecor;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.Collection;

@UtilityClass
public class WorldGuardUtils {
    public boolean checkFlag(Player player, StateFlag flag, StateFlag.State state) {
        try {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            LocalPlayer local = WorldGuardPlugin.inst().wrapPlayer(player);
            ApplicableRegionSet set = query.getApplicableRegions(local.getLocation());
            Collection<StateFlag.State> playerFlags = set.queryAllValues(local, flag);

            return playerFlags.contains(state);
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }
}