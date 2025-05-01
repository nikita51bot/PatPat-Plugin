package net.lopymine.patpat.plugin;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import net.lopymine.patpat.plugin.command.PatPatCommandManager;
import net.lopymine.patpat.plugin.config.PatPatConfig;
import net.lopymine.patpat.plugin.config.PlayerListConfig;
import net.lopymine.patpat.plugin.event.PatPatPlayerEventHandler;
import net.lopymine.patpat.plugin.packet.PatPatPacketManager;

@Getter
public final class PatPatPlugin extends JavaPlugin {

	public static final String MOD_ID = "patpat-plugin";

	@Getter
	private static PatPatPlugin instance;

	@Override
	@SuppressWarnings("java:S2696") // Plugins system
	public void onEnable() {
		instance = this;

		if (!this.getDataFolder().exists() && !this.getDataFolder().mkdirs()) {
			PatLogger.warn("Failed to create data folder for PatPat Plugin!");
		}
		PatPatConfig.reload();
		PlayerListConfig.reload();

		PatPatPacketManager.register();
		PatPatCommandManager.register();
		PatPatPlayerEventHandler.register();


		PatLogger.info("Plugin started");
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
