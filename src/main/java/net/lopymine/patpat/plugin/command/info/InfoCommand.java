package net.lopymine.patpat.plugin.command.info;

import lombok.experimental.ExtensionMethod;
import org.bukkit.command.CommandSender;

import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import net.lopymine.patpat.plugin.util.StringUtils;

import java.util.Collections;
import java.util.List;

@ExtensionMethod(CommandSenderExtension.class)
public class InfoCommand implements ICommand {

	@Override
	public List<String> getSuggestions(CommandSender commandSender, String[] strings) {
		return Collections.emptyList();
	}

	@Override
	public void execute(CommandSender commandSender, String[] strings) {
		commandSender.sendPatPatMessage("Platform: Bukkit");
		commandSender.sendPatPatMessage("Version: " + PatPatPlugin.getInstance().getDescription().getVersion());
	}

	@Override
	public String getPermissionKey() {
		return StringUtils.permission("info");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat info";
	}

	@Override
	public String getDescription() {
		return "Print information about plugin version";
	}
}