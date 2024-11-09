package com.loficostudios.christmasTreeDecor.listeners;

import com.loficostudios.christmasTreeDecor.ChristmasTreeDecor;
import com.loficostudios.christmasTreeDecor.messages.Messages;
import com.loficostudios.christmasTreeDecor.managers.ProfileManager;
import com.loficostudios.christmasTreeDecor.records.Ornament;
import com.loficostudios.christmasTreeDecor.utils.ColorUtil;
import com.loficostudios.christmasTreeDecor.utils.Common;
import com.loficostudios.christmasTreeDecor.utils.Debug;
import com.loficostudios.christmasTreeDecor.utils.WorldGuardUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class ChristmasTreeListener implements Listener {

    private final ChristmasTreeDecor plugin;

    private final ProfileManager profileManager;

    public ChristmasTreeListener(ChristmasTreeDecor plugin, ProfileManager profileManager) {
        this.plugin = plugin;
        this.profileManager = profileManager;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    private void christmasTreeInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (WorldGuardUtils.isPlayerInRegionWithFlag(player)) {

            Block block = e.getClickedBlock();
            ItemStack itemInHand = e.getItem();
            BlockFace blockFace = e.getBlockFace();

            if (block == null || itemInHand == null)
                return;

            Vector direction = blockFace.getDirection();
            Location origin = block.getLocation();

            Block targetBlock = new Location(origin.getWorld(),
                    origin.getX() + direction.getX(),
                    origin.getY() + direction.getY(),
                    origin.getZ() + direction.getZ()).getBlock();

            profileManager.getProfile(player.getUniqueId()).ifPresent(profile -> {
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    e.setCancelled(true);

                    if (!hasOrnament(player, itemInHand.getItemMeta())) {
                        Debug.log("player does not have ornament in hand");
                        return;
                    }

                    if (!canPlaceOrnament(e, origin, targetBlock, direction))
                        return;

                    if (profile.addOrnament(targetBlock.getLocation())) {

                        SkullMeta meta = (SkullMeta) itemInHand.getItemMeta();
                        Ornament ornament = new Ornament(
                                meta.getOwnerProfile(),
                                targetBlock);

                        handleOrnamentPlacement(player, ornament, blockFace);
                    }

                } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    e.setCancelled(false);

                    if (block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD)) {
                        e.setCancelled(true);

                        if (profile.isPlayerOrnamentOwner(block.getLocation())) {

                        }
                    }
                }
            });
        }
    }

    private void handleOrnamentPlacement(final Player player, @NotNull Ornament ornament, BlockFace blockFace) {
        Vector direction = blockFace.getDirection();

        PlayerProfile profile = ornament.profile();
        Block ornementBlock = ornament.block();

        if (direction.getY() > 0) {
            ornementBlock.setType(Material.PLAYER_HEAD);
            Debug.log("placed ornament on floor");

            BlockState state = ornementBlock.getState();

            Skull skull = (Skull) state;

            skull.setOwnerProfile(profile);
            skull.update();

            if (ornementBlock.getBlockData() instanceof Rotatable) {
                Rotatable rotatable = (Rotatable) ornementBlock.getBlockData();

                BlockFace rotation = Common.getDirectionFromCamera(player.getLocation().getYaw());

                player.sendMessage("facing " + rotation + " yaw" + player.getLocation().getYaw());

                rotatable.setRotation(rotation.getOppositeFace());
                ornementBlock.setBlockData(rotatable);
            }
        }
        else if (direction.getY() == 0) {
            ornementBlock.setType(Material.PLAYER_WALL_HEAD);
            Debug.log("placed ornament on wall");

            BlockState state = ornementBlock.getState();

            Skull skull = (Skull) state;

            skull.setRotation(blockFace);

            skull.setOwnerProfile(profile);
            skull.update();
        }

        sendMessage(player, Messages.ORNAMENT_PLACED);
    }

    private void sendMessage(Player player, String message) {
        if (message.isEmpty())
            return;
        player.sendMessage(ColorUtil.deserialize(message));
    }

    private boolean canPlaceOrnament(PlayerInteractEvent e, Location origin, Block target, Vector direction) {
        Player player = e.getPlayer();

        if (!player.hasPermission(ChristmasTreeDecor.ORNAMENT_PERMISSION)) {
            sendMessage(player, Messages.PERMISSION_DENIED);
            return false;
        }

        boolean canPlace = !(direction.getY() < 0) && target.getType().equals(Material.AIR) && isBlockLeaveBlock(origin.getBlock());

        if (!canPlace) {
            sendMessage(player, Messages.CANNOT_PLACE_ORNAMENT);
            return false;
        }

        return true;
    }

    private boolean isBlockLeaveBlock(Block block) {
        Debug.log("is leave block? " + (block.getBlockData() instanceof Leaves));
        return (block.getBlockData() instanceof Leaves);
    }

    private boolean hasOrnament(Player player, ItemMeta meta) {
        if (meta == null)
            return false;

        boolean hasOrnament = (meta instanceof SkullMeta);

        if (!hasOrnament) {
            sendMessage(player, Messages.NO_ORNAMENT);
            return false;
        }

        return true;
    }
}