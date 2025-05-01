package net.lopymine.patpat.plugin.command.list;

import lombok.experimental.ExtensionMethod;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.lopymine.patpat.plugin.command.PatPatCommandManager;
import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.config.PlayerListConfig;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import net.lopymine.patpat.plugin.util.StringUtils;

import java.util.*;

@ExtensionMethod(CommandSenderExtension.class)
public class ListAddCommand implements ICommand {

	@Override
	public List<String> getSuggestions(CommandSender sender, String[] strings) {
		if (strings.length == 1) {
			return Bukkit.getOnlinePlayers().stream()
					.map(Player::getName)
					.filter(name -> name.startsWith(strings[0]))
					.toList();
		}
		return Collections.emptyList();
	}

	@Override
	public void execute(CommandSender sender, String[] strings) { // TODO: rewrite method for servers, with `online-mode: false`, problem with uuid
		if (strings.length == 0) {
			sender.sendPatPatMessage(PatPatCommandManager.getWrongMessage("command"));
			sender.sendPatPatMessage(this.getExampleOfUsage());
		}

		String value = strings[0];
		OfflinePlayer offlinePlayer = null;
		try {
			UUID uuid = UUID.fromString(value);
			offlinePlayer = Bukkit.getOfflinePlayer(uuid);
		} catch (IllegalArgumentException ignored) {
			// input is not uuid type
		}

		for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			if (value.equals(player.getName())) {
				offlinePlayer = player;
				break;
			}
		}

		if (offlinePlayer == null) {
			sender.sendPatPatMessage("Failed to find player with \"§6%s§r\" uuid or nickname", value);
			return;
		}

		PlayerListConfig config = PlayerListConfig.getInstance();
		Set<UUID> uuids = config.getUuids();
		String name = offlinePlayer.getName();
		if (name == null) {
			name = "null";
		}
		if (uuids.add(offlinePlayer.getUniqueId())) {
			sender.sendPatPatMessage("Player §6%s§r has been added to list", name);
		} else {
			sender.sendPatPatMessage("Player §6%s§r already added to list!", name);
		}
		config.save();
	}

	@Override
	public String getPermissionKey() {
		return StringUtils.permission("list.add");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat list add [<UUID> | <NICKNAME>]";
	}

	@Override
	public String getDescription() {
		return "Adds player to the permission list";
	}
}
