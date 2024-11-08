package com.loficostudios.christmasTreeDecor.listeners;

import com.loficostudios.christmasTreeDecor.ChristmasTreeDecor;
import com.loficostudios.christmasTreeDecor.Messages;
import com.loficostudios.christmasTreeDecor.utils.ColorUtil;
import com.loficostudios.christmasTreeDecor.utils.Debug;
import com.loficostudios.christmasTreeDecor.utils.WorldGuardUtils;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
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

import java.util.Objects;

public class ChristmasTreeListener implements Listener {

    private final ChristmasTreeDecor plugin;

    public ChristmasTreeListener(ChristmasTreeDecor plugin) {
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    private void christmasTreeInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        ItemStack itemInHand = e.getItem();
        BlockFace blockFace = e.getBlockFace();

        if (player.isOp()
                || block == null
                || !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                || itemInHand == null)
            return;

        if (!hasOrnament(player, itemInHand.getItemMeta())) {
            Debug.log("player does not have ornament in hand");
            return;
        }

        Vector direction = blockFace.getDirection();
        Location origin = block.getLocation();

        Block targetBlock = new Location(origin.getWorld(),
                origin.getX() + direction.getX(),
                origin.getY() + direction.getY(),
                origin.getZ() + direction.getZ()).getBlock();
        if (!canPlaceOrnament(e, origin, targetBlock, direction))
            return;

        SkullMeta meta = (SkullMeta) itemInHand.getItemMeta();
        Ornament ornament = new Ornament(
                meta.getOwnerProfile(),
                targetBlock);

        handleOrnamentPlacement(player, ornament, blockFace);
    }

    private void handleOrnamentPlacement(Player player, Ornament ornament, BlockFace blockFace) {
        Vector direction = blockFace.getDirection();

        PlayerProfile profile = ornament.getProfile();
        Block ornementBlock = ornament.getBlock();

        if (direction.getY() > 0) {
            ornementBlock.setType(Material.PLAYER_HEAD);
            Debug.log("placed ornament on floor");

            BlockState state = ornementBlock.getState();

            Skull skull = (Skull) state;

            skull.setOwnerProfile(profile);
            skull.update();
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

        if (WorldGuardUtils.checkFlag(player, ChristmasTreeDecor.ALLOW_ORNAMENTS, StateFlag.State.DENY)) {
            //sendMessage(player, Messages.NOT_IN_REGION);
            return false;
        }
        e.setCancelled(true);

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