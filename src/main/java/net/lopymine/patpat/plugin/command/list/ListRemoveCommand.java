package net.lopymine.patpat.plugin.command.list;

import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.command.PatPatCommandManager;
import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.config.PlayerListConfig;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ExtensionMethod(CommandSenderExtension.class)
public class ListRemoveCommand implements ICommand {

	@Override
	public List<String> getSuggestions(CommandSender commandSender, String[] strings) {
		if (strings.length == 1) {
			return Bukkit.getOnlinePlayers().stream()
					.map(Player::getName)
					.filter(name -> name.startsWith(strings[0]))
					.toList();
		}
		return Collections.emptyList();
	}

	@Override
	public void execute(CommandSender sender, String[] strings) { // TODO: rewrite method for servers, with `online-mode: false`
		if (strings.length == 0) {
			sender.sendPatPatMessage(PatPatCommandManager.getWrongMessage("command"));
			sender.sendPatPatMessage(this.getExampleOfUsage());
			return;
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
			sender.sendPatPatMessage("Failed to find player with \"%s\" uuid or nickname", value);
			return;
		}

		PlayerListConfig config = PatPatPlugin.getInstance().getPlayerListConfig();
		Set<UUID> uuids = config.getUuids();
		String name = offlinePlayer.getName();
		if (name == null) {
			name = "null";
		}
		if (uuids.remove(offlinePlayer.getUniqueId())) {
			sender.sendPatPatMessage("Player §6%s§r has been removed to list", name);
		} else {
			sender.sendPatPatMessage("Player §6%s§r was not found in list", name);
		}
		config.save();
	}

	@Override
	public String getPermissionKey() {
		return PatPatPlugin.permission("list.remove");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat list remove (<UUID> | <NICKNAME>)";
	}

	@Override
	public String getDescription() {
		return "Removes player from the permission list";
	}
}
