package net.lopymine.patpat.plugin.extension;

import net.lopymine.patpat.plugin.command.PatPatCommandManager;
import org.bukkit.command.CommandSender;

public class CommandSenderExtension {

	private CommandSenderExtension() {
		throw new IllegalStateException("Extension class");
	}

	public static void sendPatPatMessage(CommandSender sender, String message, Object... args) {
		sender.sendMessage(PatPatCommandManager.getPluginMessage(message.formatted(args)));
	}

}
