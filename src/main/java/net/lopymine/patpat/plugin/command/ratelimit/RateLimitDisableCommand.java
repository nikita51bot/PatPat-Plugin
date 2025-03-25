package net.lopymine.patpat.plugin.command.ratelimit;

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
public class RateLimitDisableCommand implements ICommand {

	@Override
	public List<String> getSuggestions(CommandSender sender, String[] strings) {
		return Collections.emptyList();
	}

	@Override
	public void execute(CommandSender sender, String[] strings) {
		PatPatConfig config = PatPatConfig.getInstance();
		RateLimitConfig rateLimitConfig = config.getRateLimit();
		if (!rateLimitConfig.isEnabled()) {
			sender.sendPatPatMessage("RateLimit is already disabled");
			return;
		}
		rateLimitConfig.setEnabled(false);
		config.save();
		RateLimitManager.reloadTask();
		sender.sendPatPatMessage("RateLimit disabled");
	}

	@Override
	public String getPermissionKey() {
		return PatPatPlugin.permission("ratelimit.disable");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat ratelimit disable";
	}


	@Override
	public String getDescription() {
		return "Disabling ratelimit";
	}
}