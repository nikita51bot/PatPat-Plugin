package net.lopymine.patpat.plugin.command.ratelimit.set;

import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.config.RateLimitConfig;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@ExtensionMethod(CommandSenderExtension.class)
public class LimitCommand implements ICommand {
	@Override
	public List<String> getSuggestions(CommandSender sender, String[] strings) {
		return Collections.emptyList();
	}

	@Override
	public void execute(CommandSender sender, String[] strings) {
		PatPatConfig config = PatPatConfig.getInstance();
		RateLimitConfig rateLimitConfig = config.getRateLimit();
		if (strings.length == 0) {
			sender.sendPatPatMessage("Token Limit: " + rateLimitConfig.getTokenLimit());
			return;
		}
		if (strings.length > 1) {
			sender.sendPatPatMessage(this.getExampleOfUsage());
			return;
		}
		try {
			int value = Integer.parseInt(strings[0]);
			if (value <= 0) {
				sender.sendPatPatMessage("Limit '%s' can't be less than 1", value);
				return;
			}
			rateLimitConfig.setTokenLimit(value);
			config.save();
			sender.sendPatPatMessage("Set token limit: " + value);
		} catch (NumberFormatException ignored) {
			sender.sendPatPatMessage("'%s' is not number", strings[0]);
		}
	}

	@Override
	public String getPermissionKey() {
		return PatPatPlugin.permission("ratelimit.set.limit");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat ratelimit set limit [value]";
	}

	@Override
	public String getDescription() {
		return "Set token limit";
	}
}
