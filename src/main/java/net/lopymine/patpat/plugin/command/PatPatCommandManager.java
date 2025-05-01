package net.lopymine.patpat.plugin.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.command.api.*;
import net.lopymine.patpat.plugin.command.info.InfoCommand;
import net.lopymine.patpat.plugin.command.list.*;
import net.lopymine.patpat.plugin.command.ratelimit.*;
import net.lopymine.patpat.plugin.command.ratelimit.set.*;
import net.lopymine.patpat.plugin.command.reload.ReloadCommand;
import net.lopymine.patpat.plugin.util.StringUtils;

import java.util.Objects;

public class PatPatCommandManager {

	private PatPatCommandManager() {
		throw new IllegalStateException("Manager class");
	}

	public static void register() {
		SimpleCommand listCommand = registerListCommand();
		SimpleCommand rateLimitCommand = registerRateLimitCommand();
		SimpleCommand reloadCommand = getSimpleCommand(new ReloadCommand());
		SimpleCommand infoCommand = getSimpleCommand(new InfoCommand());

		SimpleCommand rootCommand = SimpleCommand.builder()
				.usage(PatPatCommandManager.getPluginMessage("/patpat (list | ratelimit | reload)"))
				.child(listCommand, "list")
				.child(rateLimitCommand, "ratelimit")
				.child(reloadCommand, "reload")
				.child(infoCommand, "info")
				.build();

		PatPatPlugin plugin = PatPatPlugin.getInstance();
		PluginCommand command = plugin.getCommand("patpat");
		Objects.requireNonNull(command, "Command `patpat` is not registered");
		command.setExecutor(rootCommand);
	}

	private static SimpleCommand registerListCommand() {
		SimpleCommand setModeCommand = getSimpleCommand(new ListSetCommand());
		SimpleCommand addToListCommand = getSimpleCommand(new ListAddCommand());
		SimpleCommand removeFromListCommand = getSimpleCommand(new ListRemoveCommand());

		return SimpleCommand.builder()
				.permission(StringUtils.permission("list"))
				.usage("/patpat list (set | add | remove)")
				.child(setModeCommand, "set")
				.child(addToListCommand, "add")
				.child(removeFromListCommand, "remove")
				.build();
	}

	private static SimpleCommand registerRateLimitCommand() {
		SimpleCommand infoCommand = getSimpleCommand(new RateLimitInfoCommand());
		SimpleCommand enableCommand = getSimpleCommand(new RateLimitEnableCommand());
		SimpleCommand disableCommand = getSimpleCommand(new RateLimitDisableCommand());

		SimpleCommand incrementCommand = getSimpleCommand(new IncrementCommand());
		SimpleCommand intervalCommand = getSimpleCommand(new IntervalCommand());
		SimpleCommand limitCommand = getSimpleCommand(new LimitCommand());

		SimpleCommand setCommand = SimpleCommand.builder()
				.permission(StringUtils.permission("ratelimit.set"))
				.usage("/patpat ratelimit set (increment | interval | limit)")
				.child(incrementCommand, "increment")
				.child(intervalCommand, "interval")
				.child(limitCommand, "limit")
				.build();

		return SimpleCommand.builder()
				.permission(StringUtils.permission("ratelimit"))
				.usage("/patpat ratelimit (enable | disable | set | info)")
				.child(enableCommand, "enable", "on")
				.child(disableCommand, "disable", "off")
				.child(infoCommand, "info")
				.child(setCommand, "set")
				.build();
	}

	public static SimpleCommand getSimpleCommand(ICommand command) {
		return SimpleCommand.builder()
				.permission(command.getPermissionKey())
				.usage(command.getExampleOfUsage())
				.msgNoPermission(PatPatCommandManager.getNoPermissionMessage())
				.description(command.getDescription())
				.executor(command)
				.build();
	}

	public static SimpleCommand getSimpleCommand(ICommand command, ChildCommand... childCommands) {
		SimpleCommand.Builder simpleCommandBuilder = SimpleCommand.builder()
				.permission(command.getPermissionKey())
				.usage(command.getExampleOfUsage())
				.msgNoPermission(PatPatCommandManager.getNoPermissionMessage())
				.description(command.getDescription())
				.executor(command);

		for (ChildCommand childCommand : childCommands) {
			simpleCommandBuilder.child(childCommand.getCommand(), childCommand.getName(), childCommand.getAliases());
		}
		return simpleCommandBuilder.build();
	}

	public static void sendMessage(CommandSender sender, String message, Object... args) {
		sender.sendMessage(getPluginMessage(message.formatted(args)));
	}

	public static String getNoPermissionMessage() {
		return getPluginMessage("You don't have permissions to execute this command!");
	}

	public static String getWrongMessage(String valueWhichIsWrong) {
		return "Was entered wrong %s, please follow this example of usage:".formatted(valueWhichIsWrong);
	}

	public static String getPluginMessage(String message) {
		return "[§aPatPat§f] " + message;
	}


}
