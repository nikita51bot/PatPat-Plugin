package net.lopymine.patpat.plugin.extension;

import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.command.PatPatCommandManager;

public class CommandSenderExtension {

	private static final TextComponent PREFIX_COMPONENT = Component
			.text("[")
			.append(Component.text("PatPat").color(NamedTextColor.GREEN))
			.append(Component.text("] "));

	private CommandSenderExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static void sendPatPatMessage(CommandSender sender, String message, Object... args) {
		sender.sendMessage(PatPatCommandManager.getPluginMessage(message.formatted(args)));
	}

	public static void sendPatPatMessage(CommandSender sender, ComponentLike message) {
		PatPatPlugin.getAdventure().sender(sender).sendMessage(PREFIX_COMPONENT.append(message));
	}

	public static void sendTranslatable(CommandSender sender, String key, ComponentLike... args) {
		PatPatPlugin.getAdventure().sender(sender).sendMessage(PREFIX_COMPONENT.append(Component.translatable(key).args(args)));
	}

}
