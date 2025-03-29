package net.lopymine.patpat.plugin.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.config.RateLimitConfig;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@ExtensionMethod(CommandSenderExtension.class)
public class RateLimitInfoCommand implements ICommand {
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
	public void execute(CommandSender sender, String[] strings) {
		RateLimitConfig config = PatPatConfig.getInstance().getRateLimit();
		if (strings.length > 0) {
			Player player = Bukkit.getPlayer(strings[0]);
			if (player == null) {
				sender.sendPatPatMessage("Player '%s' is not exist", strings[0]);
				return;
			}
			sender.sendPatPatMessage("Info '%s'", strings[0]);

			int availablePats = RateLimitManager.getAvailablePats(player.getUniqueId());
			String message = "Tokens: %d";
			if (player.hasPermission(config.getPermissionBypass())) {
				message = "Tokens: bypass";
			}
			sender.sendPatPatMessage(message, availablePats);
			return;
		}

		sender.sendPatPatMessage("Enabled: " + config.isEnabled());
		sender.sendPatPatMessage("Token limit: " + config.getTokenLimit());
		sender.sendPatPatMessage("Token increment: " + config.getTokenIncrement());
		sender.sendPatPatMessage("Token increment interval: " + config.getTokenIncrementInterval().toString());
		sender.sendPatPatMessage("Permission bypass: " + config.getPermissionBypass());
	}

	@Override
	public String getPermissionKey() {
		return PatPatPlugin.permission("ratelimit.info");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat ratelimit info [<player>]";
	}

	@Override
	public String getDescription() {
		return "Get info about ratelimit params";
	}
}
