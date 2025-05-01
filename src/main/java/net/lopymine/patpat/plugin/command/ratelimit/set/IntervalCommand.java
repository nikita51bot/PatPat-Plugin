package net.lopymine.patpat.plugin.command.ratelimit.set;

import lombok.experimental.ExtensionMethod;
import org.bukkit.command.CommandSender;

import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.command.ratelimit.RateLimitManager;
import net.lopymine.patpat.plugin.command.ratelimit.Time;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.config.RateLimitConfig;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import net.lopymine.patpat.plugin.util.StringUtils;

import java.util.Collections;
import java.util.List;

@ExtensionMethod(CommandSenderExtension.class)
public class IntervalCommand implements ICommand {

	@Override
	public List<String> getSuggestions(CommandSender sender, String[] strings) {
		return Collections.emptyList();
	}

	@Override
	public void execute(CommandSender sender, String[] strings) {
		PatPatConfig config = PatPatConfig.getInstance();
		RateLimitConfig rateLimitConfig = config.getRateLimit();
		if (strings.length == 0) {
			sender.sendPatPatMessage("Token Interval: " + rateLimitConfig.getTokenIncrementInterval());
			return;
		}
		if (strings.length > 1) {
			sender.sendPatPatMessage(this.getExampleOfUsage());
			return;
		}
		try {
			Time value = Time.of(strings[0]);
			if (value.getValue() <= 0) {
				sender.sendPatPatMessage("Time '%s' can't be less than 1", value);
				return;
			}
			rateLimitConfig.setTokenIncrementInterval(value);
			config.save();
			RateLimitManager.reloadTask();
			sender.sendPatPatMessage("Set token interval: " + value);
		} catch (IllegalArgumentException ignored) {
			sender.sendPatPatMessage("'%s' is not time value (examples: 1sec, 5s, 5min...)", strings[0]);
		}
	}

	@Override
	public String getPermissionKey() {
		return StringUtils.permission("ratelimit.set.interval");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat ratelimit set interval [value]";
	}

	@Override
	public String getDescription() {
		return "Set token interval";
	}

}
