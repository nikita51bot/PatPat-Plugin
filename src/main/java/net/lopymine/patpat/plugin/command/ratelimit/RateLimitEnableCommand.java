package net.lopymine.patpat.plugin.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.config.RateLimitConfig;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import net.lopymine.patpat.plugin.util.StringUtils;

import java.util.Collections;
import java.util.List;

@ExtensionMethod(CommandSenderExtension.class)
public class RateLimitEnableCommand implements ICommand {

	private static final Component ENABLED = Component.translatable("patpat.command.ratelimit.enabled").color(NamedTextColor.GREEN);

	@Override
	public List<String> getSuggestions(CommandSender sender, String[] strings) {
		return Collections.emptyList();
	}

	@Override
	public void execute(CommandSender sender, String[] strings) {
		PatPatConfig config = PatPatConfig.getInstance();
		RateLimitConfig rateLimitConfig = config.getRateLimit();
		if (rateLimitConfig.isEnabled()) {
			sender.sendTranslatable("patpat.command.ratelimit.toggle.already", ENABLED);
			return;
		}
		rateLimitConfig.setEnabled(true);
		config.save();
		RateLimitManager.reloadTask();
		sender.sendTranslatable("patpat.command.ratelimit.toggle", ENABLED);
	}

	@Override
	public String getPermissionKey() {
		return StringUtils.permission("ratelimit.toggle");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat ratelimit enable";
	}


	@Override
	public Component getDescription() {
		return Component.translatable("patpat.command.ratelimit.enable.description");
	}
}
