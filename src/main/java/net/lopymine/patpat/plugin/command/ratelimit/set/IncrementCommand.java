package net.lopymine.patpat.plugin.command.ratelimit.set;

import lombok.experimental.ExtensionMethod;
import org.bukkit.command.CommandSender;

import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.config.RateLimitConfig;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import net.lopymine.patpat.plugin.util.StringUtils;

import java.util.Collections;
import java.util.List;

@ExtensionMethod(CommandSenderExtension.class)
public class IncrementCommand implements ICommand {

	@Override
	public List<String> getSuggestions(CommandSender sender, String[] strings) {
		return Collections.emptyList();
	}

	@Override
	public void execute(CommandSender sender, String[] strings) {
		PatPatConfig config = PatPatConfig.getInstance();
		RateLimitConfig rateLimitConfig = config.getRateLimit();
		if (strings.length == 0) {
			sender.sendPatPatMessage("Token Increment: " + rateLimitConfig.getTokenIncrement());
			return;
		}
		if (strings.length > 1) {
			sender.sendPatPatMessage(this.getExampleOfUsage());
			return;
		}
		try {
			int value = Integer.parseInt(strings[0]);
			if (value <= 0) {
				sender.sendPatPatMessage("Increment '%s' can't be less than 1", value);
				return;
			}
			rateLimitConfig.setTokenIncrement(value);
			config.save();
			sender.sendPatPatMessage("Set token increment: " + value);
		} catch (NumberFormatException ignored) {
			sender.sendPatPatMessage("'%s' is not number", strings[0]);
		}
	}

	@Override
	public String getPermissionKey() {
		return StringUtils.permission("ratelimit.set.increment");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat ratelimit set increment [value]";
	}

	@Override
	public String getDescription() {
		return "Set token increment";
	}

}
