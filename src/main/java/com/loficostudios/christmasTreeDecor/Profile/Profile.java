/**
 * @Author Tonierbobcat
 * @Github https://github.com/Tonierbobcat
 * @version SoulBoundSMPCore
 * @since 6/12/2024
 */

package com.loficostudios.christmasTreeDecor.Profile;

import com.loficostudios.christmasTreeDecor.ChristmasTreeDecor;
import com.loficostudios.christmasTreeDecor.messages.Messages;
import com.loficostudios.christmasTreeDecor.file.impl.YamlFile;
import com.loficostudios.christmasTreeDecor.utils.ColorUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.loficostudios.christmasTreeDecor.config.Config.MAX_ORNAMENTS_PLAYER_CAN_PLACE;

public class Profile {




	private final YamlFile playerFile;
	private final FileConfiguration playerConfig;

	@Getter
	private final UUID uuid;

	private int ornamentsPlaced = 0;

	private List<Location> playerOrnamentLocations = new ArrayList<>();

	public Profile(UUID uuid) {
		this.uuid = uuid;

		this.playerFile = new YamlFile("players/" + uuid + ".yml", ChristmasTreeDecor.getInstance());
		this.playerConfig = playerFile.getConfig();

		ornamentsPlaced = playerConfig.getInt("ornamentsPlaced");


		playerOrnamentLocations = (List<Location>) playerConfig.get("locations", playerOrnamentLocations);

		playerConfig.set("uuid", uuid.toString());

		saveOrnaments();

		playerFile.save();
	}



	private void saveOrnaments() {
		playerConfig.set("ornamentsPlaced", ornamentsPlaced);
		playerConfig.set("locations", playerOrnamentLocations);
		playerFile.save();
	}

	public boolean isPlayerOrnamentOwner(Location location) {

		Block ornamentBlock = location.getBlock();

//		getPlayer().sendMessage("" + ornamentBlock);
//
//		playerOrnamentLocations.forEach(location1 -> {
//			getPlayer().sendMessage("" + location1);
//		});

		if (!playerOrnamentLocations.contains(location)) {

			getPlayer().sendMessage(ColorUtil.deserialize(
					Messages.ORNAMENT_REMOVE_DENY
			));
			return false;
		}

		ornamentBlock.setType(Material.AIR);

		playerOrnamentLocations.remove(location);
		ornamentsPlaced--;
		saveOrnaments();

		getPlayer().sendMessage(ColorUtil.deserialize(
				Messages.ORNAMENT_REMOVE_ALLOW
		));
		return true;
	}

	public boolean addOrnament(Location location) {
		if (ornamentsPlaced < MAX_ORNAMENTS_PLAYER_CAN_PLACE) {
			ornamentsPlaced++;

			playerOrnamentLocations.add(location);
			ChristmasTreeDecor.getInstance().getGlobalOrnamentLocations().add(location);
			saveOrnaments();
			return true;
		}
		else {
			getPlayer().sendMessage(ColorUtil.deserialize(
					Messages.MAX_ORNAMENTS.replace("{max}", String.valueOf(MAX_ORNAMENTS_PLAYER_CAN_PLACE))
			));
			return false;
		}
	}

	public void saveData() {

	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}
}
