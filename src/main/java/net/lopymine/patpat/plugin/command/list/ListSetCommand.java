package net.lopymine.patpat.plugin.command.list;

import lombok.experimental.ExtensionMethod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.config.option.ListMode;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import net.lopymine.patpat.plugin.util.StringUtils;

import java.util.Collections;
import java.util.List;

@ExtensionMethod(CommandSenderExtension.class)
public class ListSetCommand implements ICommand {

	private static final List<String> LIST_MODES = List.of("WHITELIST", "BLACKLIST", "DISABLED");

	@Override
	public List<String> getSuggestions(CommandSender sender, String[] strings) {
		if (strings.length != 1) {
			return Collections.emptyList();
		}
		String query = strings[0].toLowerCase();
		return LIST_MODES
				.stream()
				.filter(mode -> mode.toLowerCase().startsWith(query))
				.toList();
	}

	@Override
	public void execute(CommandSender sender, String[] strings) {
		if (strings.length == 0) {
			sender.sendPatPatMessage(this.getExampleOfUsage());
			return;
		}

		String value = strings[0].toUpperCase();
		try {
			ListMode listMode = ListMode.valueOf(value);
			PatPatConfig config = PatPatConfig.getInstance();
			if (config.getListMode().equals(listMode)) {
				TextComponent listComponent = Component
						.text(listMode.name())
						.color(NamedTextColor.GOLD);
				sender.sendTranslatable("patpat.command.list.set.already", listComponent);
				return;
			}
			config.setListMode(listMode);
			config.save();
			TextComponent listComponent = Component
					.text(listMode.name())
					.color(NamedTextColor.GOLD);
			sender.sendTranslatable("patpat.command.list.set", listComponent);
		} catch (IllegalArgumentException e) {
			sender.sendTranslatable("patpat.command.list.set.error", Component.text(value).color(NamedTextColor.GOLD));
		}
	}

	@Override
	public String getPermissionKey() {
		return StringUtils.permission("list.set");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat list set (WHITELIST | BLACKLIST | DISABLED)";
	}

	@Override
	public Component getDescription() {
		return Component.translatable("patpat.command.list.set.description");
	}
}
