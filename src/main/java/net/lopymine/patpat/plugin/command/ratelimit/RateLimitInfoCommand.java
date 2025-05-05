package net.lopymine.patpat.plugin.command.ratelimit;

import lombok.experimental.ExtensionMethod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.ClickEvent.Action;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.config.RateLimitConfig;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import net.lopymine.patpat.plugin.util.StringUtils;

import java.util.Collections;
import java.util.List;

@ExtensionMethod(CommandSenderExtension.class)
public class RateLimitInfoCommand implements ICommand {

	private static final Component DISABLED = Component.translatable("patpat.command.ratelimit.disabled").color(NamedTextColor.RED);
	private static final Component ENABLED = Component.translatable("patpat.command.ratelimit.enabled").color(NamedTextColor.GREEN);

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
				sender.sendTranslatable(
						"patpat.command.error.player.not_exist",
						Component.text(strings[0]).color(NamedTextColor.GOLD)
				);
				return;
			}
			sender.sendTranslatable(
					"patpat.command.ratelimit.info.player",
					Component.text(strings[0]).color(NamedTextColor.GOLD)
			);

			Component tokensComponent;
			if (player.hasPermission(config.getPermissionBypass())) {
				tokensComponent = Component.translatable("patpat.command.ratelimit.info.tokens.bypass");
			} else {
				tokensComponent = Component.text(RateLimitManager.getAvailablePats(player.getUniqueId()));
			}
			tokensComponent = tokensComponent.color(NamedTextColor.GOLD);
			sender.sendTranslatable("patpat.command.ratelimit.info.tokens", tokensComponent);
			return;
		}

		Component statusComponent = config.isEnabled() ? ENABLED : DISABLED;
		Component limitComponent = Component.text(config.getTokenLimit()).color(NamedTextColor.GOLD);
		Component incrementComponent = Component.text(config.getTokenIncrement()).color(NamedTextColor.GOLD);
		Component intervalComponent = Component.text(config.getTokenInterval().toString()).color(NamedTextColor.GOLD);
		Component permissionComponent = Component.text(config.getPermissionBypass())
				.color(NamedTextColor.GOLD)
				.clickEvent(ClickEvent.clickEvent(Action.COPY_TO_CLIPBOARD, config.getPermissionBypass()))
				.hoverEvent(HoverEvent.showText(Component.translatable("patpat.command.ratelimit.info.permission_bypass.copy")));

		sender.sendTranslatable("patpat.command.ratelimit.info.status", statusComponent);
		sender.sendTranslatable("patpat.command.ratelimit.set.limit", limitComponent);
		sender.sendTranslatable("patpat.command.ratelimit.set.increment", incrementComponent);
		sender.sendTranslatable("patpat.command.ratelimit.set.interval", intervalComponent);
		sender.sendTranslatable("patpat.command.ratelimit.info.permission_bypass", permissionComponent);
	}

	@Override
	public String getPermissionKey() {
		return StringUtils.permission("ratelimit.info");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat ratelimit info [<player>]";
	}

	@Override
	public Component getDescription() {
		return Component.translatable("patpat.command.ratelimit.info.description");
	}
}
