package net.lopymine.patpat.plugin.command.reload;

import lombok.experimental.ExtensionMethod;
import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@ExtensionMethod(CommandSenderExtension.class)
public class ReloadCommand implements ICommand {

	@Override
	public List<String> getSuggestions(CommandSender sender, String[] strings) {
		return Collections.emptyList();
	}

	@Override
	public void execute(CommandSender sender, String[] strings) {
		PatPatConfig.reload();
		sender.sendPatPatMessage("Successful reload config");
	}

	@Override
	public String getPermissionKey() {
		return PatPatPlugin.permission("reload");
	}

	@Override
	public String getExampleOfUsage() {
		return "/patpat reload";
	}


	@Override
	public String getDescription() {
		return "Reload config";
	}
}
