package net.lopymine.patpat.plugin.command.info;

import lombok.experimental.ExtensionMethod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.ClickEvent.Action;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import net.lopymine.patpat.plugin.PatPatPlugin;
import net.lopymine.patpat.plugin.command.api.ICommand;
import net.lopymine.patpat.plugin.extension.CommandSenderExtension;
import net.lopymine.patpat.plugin.util.StringUtils;

import java.util.Collections;
import java.util.List;

@ExtensionMethod(CommandSenderExtension.class)
public class InfoCommand implements ICommand {

	private static final String PLATFORM = "Bukkit";

	private final Component platformComponent;
	private final Component pluginVersionComponent;
	private final Component minecraftVersionComponent;

	public InfoCommand() {
		String pluginVersion = PatPatPlugin.getInstance().getDescription().getVersion();
		String minecraftVersion = Bukkit.getServer().getVersion();
		String debugInformation = "Platform: %s%nMinecraft: %s%nVersion: %s"
				.formatted(PLATFORM, minecraftVersion, pluginVersion);
		ClickEvent clickEvent = ClickEvent.clickEvent(Action.COPY_TO_CLIPBOARD, debugInformation);
		HoverEvent<Component> hoverEvent = HoverEvent.showText(Component.translatable("patpat.command.info.copy"));
		Component style = Component.empty().clickEvent(clickEvent).hoverEvent(hoverEvent);

		this.platformComponent         = Component.translatable("patpat.command.info.platform")
				.args(Component.text(PLATFORM).color(NamedTextColor.GOLD))
				.mergeStyle(style);
		this.pluginVersionComponent    = Component.translatable("patpat.command.info.plugin_version")
				.args(Component.text(pluginVersion).color(NamedTextColor.GOLD))
				.mergeStyle(style);
		this.minecraftVersionComponent = Component.translatable("patpat.command.info.minecraft_version")
				.args(Component.text(minecraftVersion).color(NamedTextColor.GOLD))
				.mergeStyle(style);
	}

	@Override
	public List<String> getSuggestions(CommandSender commandSender, String[] strings) {
		return Collections.emptyList();
	}

	@Override
	public void execute(CommandSender sender, String[] strings) {
		sender.sendPatPatMessage(platformComponent);
		sender.sendPatPatMessage(minecraftVersionComponent);
		sender.sendPatPatMessage(pluginVersionComponent);
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
	public Component getDescription() {
		return Component.translatable("patpat.command.info.description");
	}
}