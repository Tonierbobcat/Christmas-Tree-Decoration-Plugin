package com.loficostudios.christmasTreeDecor.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.block.BlockFace;

@UtilityClass
public class Common {

    public static BlockFace getDirectionFromCamera(float yaw) {
        // Normalize yaw to a range of 0° to 360°
        yaw = (yaw % 360 + 360) % 360; // Converts negative yaw to positive equivalent

        // Divide by 22.5° to get one of the 16 possible directions
        int angle = Math.round(yaw / 22.5f); // 360° / 16 directions = 22.5° per direction

        switch (angle) {
            case 0:
            case 16:
                return BlockFace.NORTH;
            case 1:
                return BlockFace.NORTH_NORTH_EAST;
            case 2:
                return BlockFace.NORTH_EAST;
            case 3:
                return BlockFace.EAST_NORTH_EAST;
            case 4:
                return BlockFace.EAST;
            case 5:
                return BlockFace.EAST_SOUTH_EAST;
            case 6:
                return BlockFace.SOUTH_EAST;
            case 7:
                return BlockFace.SOUTH_SOUTH_EAST;
            case 8:
                return BlockFace.SOUTH;
            case 9:
                return BlockFace.SOUTH_SOUTH_WEST;
            case 10:
                return BlockFace.SOUTH_WEST;
            case 11:
                return BlockFace.WEST_SOUTH_WEST;
            case 12:
                return BlockFace.WEST;
            case 13:
                return BlockFace.WEST_NORTH_WEST;
            case 14:
                return BlockFace.NORTH_WEST;
            case 15:
                return BlockFace.NORTH_NORTH_WEST;
            default:
                return BlockFace.NORTH;
        }
    }

//    public static BlockFace getDirectionFromCamera(float yaw) {
//        int angle = Math.round(yaw / 22.5f); // 360° / 16 directions = 22.5° per direction
//        switch (angle) {
//            case 0:
//            case 16:
//                return BlockFace.NORTH;
//            case 1:
//                return BlockFace.NORTH_NORTH_EAST;
//            case 2:
//                return BlockFace.NORTH_EAST;
//            case 3:
//                return BlockFace.EAST_NORTH_EAST;
//            case 4:
//                return BlockFace.EAST;
//            case 5:
//                return BlockFace.EAST_SOUTH_EAST;
//            case 6:
//                return BlockFace.SOUTH_EAST;
//            case 7:
//                return BlockFace.SOUTH_SOUTH_EAST;
//            case 8:
//                return BlockFace.SOUTH;
//            case 9:
//                return BlockFace.SOUTH_SOUTH_WEST;
//            case 10:
//                return BlockFace.SOUTH_WEST;
//            case 11:
//                return BlockFace.WEST_SOUTH_WEST;
//            case 12:
//                return BlockFace.WEST;
//            case 13:
//                return BlockFace.WEST_NORTH_WEST;
//            case 14:
//                return BlockFace.NORTH_WEST;
//            case 15:
//                return BlockFace.NORTH_NORTH_WEST;
//            default:
//                return BlockFace.NORTH;
//        }
//    }
}
