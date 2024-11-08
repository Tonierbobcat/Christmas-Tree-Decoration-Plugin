package com.loficostudios.christmasTreeDecor.listeners;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.profile.PlayerProfile;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Ornament {

    @Getter
    private final PlayerProfile profile;

    @Getter
    private final Block block;


    public Ornament(PlayerProfile profile, Block block) {
        this.profile = profile;
        this.block = block;
    }
}
