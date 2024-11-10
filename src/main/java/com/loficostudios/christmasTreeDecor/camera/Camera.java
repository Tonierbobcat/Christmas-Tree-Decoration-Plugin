package com.loficostudios.christmasTreeDecor.camera;

import com.loficostudios.christmasTreeDecor.utils.Common;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
//import org.bukkit.entity.Player;

public class Camera {

    private final Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public float getYaw() {
        return player.getLocation().getYaw();
    }

    public void setYaw(float amount) {
        player.getLocation().setYaw(amount);
    }

    public BlockFace getDirection() {

        float yaw = player.getLocation().getYaw();

        // Normalize yaw to a range of 0° to 360°
        yaw = (yaw % 360 + 360) % 360; // Converts negative yaw to positive equivalent

        // Divide by 22.5° to get one of the 16 possible directions
        int angle = Math.round(yaw / 22.5f); // 360° / 16 directions = 22.5° per direction

        return switch (angle) {
            case 1 -> BlockFace.NORTH_NORTH_EAST;
            case 2 -> BlockFace.NORTH_EAST;
            case 3 -> BlockFace.EAST_NORTH_EAST;
            case 4 -> BlockFace.EAST;
            case 5 -> BlockFace.EAST_SOUTH_EAST;
            case 6 -> BlockFace.SOUTH_EAST;
            case 7 -> BlockFace.SOUTH_SOUTH_EAST;
            case 8 -> BlockFace.SOUTH;
            case 9 -> BlockFace.SOUTH_SOUTH_WEST;
            case 10 -> BlockFace.SOUTH_WEST;
            case 11 -> BlockFace.WEST_SOUTH_WEST;
            case 12 -> BlockFace.WEST;
            case 13 -> BlockFace.WEST_NORTH_WEST;
            case 14 -> BlockFace.NORTH_WEST;
            case 15 -> BlockFace.NORTH_NORTH_WEST;
            default -> BlockFace.NORTH;
        };
    }
}
