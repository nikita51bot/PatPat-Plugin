package net.lopymine.patpat.plugin.command.ratelimit.set;

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
			sender.sendTranslatable("patpat.command.ratelimit.set.increment",
					Component.text(rateLimitConfig.getTokenIncrement())
							.color(NamedTextColor.GOLD));
			return;
		}
		if (strings.length > 1) {
			sender.sendPatPatMessage(this.getExampleOfUsage());
			return;
		}
		try {
			int value = Integer.parseInt(strings[0]);
			if (value <= 0) {
				sender.sendTranslatable(
						"patpat.command.error.number.less_than",
						Component.text(value).color(NamedTextColor.GOLD),
						Component.text(1).color(NamedTextColor.GOLD)
				);
				return;
			}
			rateLimitConfig.setTokenIncrement(value);
			config.save();

			sender.sendTranslatable(
					"patpat.command.ratelimit.set.increment.updated",
					Component.text(value).color(NamedTextColor.GOLD)
			);
		} catch (NumberFormatException ignored) {
			sender.sendTranslatable(
					"patpat.command.error.number.not_number",
					Component.text(strings[0]).color(NamedTextColor.GOLD)
			);
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
	public Component getDescription() {
		return Component.translatable("patpat.command.ratelimit.set.increment.description");
	}

}
