package net.lopymine.patpat.plugin.command.list;

import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import org.bukkit.command.CommandSender;

import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.command.PatPatCommandManager;
import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.config.option.ListMode;

import java.util.List;

@ExtensionMethod(CommandSenderExtension.class)
public class ListSetCommand implements ICommand {

	private static final List<String> LIST_MODES = List.of("WHITELIST", "BLACKLIST", "DISABLED");

	@Override
	public List<String> getSuggestions(CommandSender sender, String[] strings) {
		return LIST_MODES;
	}

	@Override
	public void execute(CommandSender sender, String[] strings) {
		if (strings.length == 0) {
			sender.sendPatPatMessage(PatPatCommandManager.getWrongMessage("command"));
			sender.sendPatPatMessage(this.getExampleOfUsage());
			return;
		}

		String value = strings[0];
		try {
			ListMode listMode = ListMode.valueOf(value.toUpperCase());
			PatPatConfig config = PatPatConfig.getInstance();
			config.setListMode(listMode);
			config.save();
			sender.sendPatPatMessage("List mode has been changed to §6%s§r", listMode.name());
		} catch (IllegalArgumentException e) {
			sender.sendPatPatMessage(PatPatCommandManager.getWrongMessage("list mode"));
			sender.sendPatPatMessage(this.getExampleOfUsage());
		}
	}

	@Override
	public String getPermissionKey() {
		return PatPatPlugin.permission("list.set");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat list set (WHITELIST | BLACKLIST | DISABLED)";
	}

	@Override
	public String getDescription() {
		return "Sets mode of the permission list";
	}
}
