package net.lopymine.patpat.plugin;

import lombok.*;
import org.bukkit.plugin.java.JavaPlugin;

import net.lopymine.patpat.plugin.command.PatPatCommandManager;
import net.lopymine.patpat.plugin.config.*;
import net.lopymine.patpat.plugin.packet.PatPatPacketManager;

import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public final class PatPatPlugin extends JavaPlugin {

	public static final String MOD_ID = "patpat";
	public static final Logger LOGGER = Logger.getLogger(MOD_ID);

	@Getter
	private static PatPatPlugin instance;
	private PlayerListConfig playerListConfig;

	public static String id(String path) {
		return "%s:%s".formatted(MOD_ID, path);
	}

	public static String permission(String permission) {
		return "%s.%s".formatted(MOD_ID, permission);
	}

	@Override
	@SuppressWarnings("java:S2696") // Plugins system
	public void onEnable() {
		instance = this;

		if (!this.getDataFolder().exists() && !this.getDataFolder().mkdirs()) {
			LOGGER.log(Level.WARNING, "Failed to create data folder for PatPat Plugin!");
		}
		PatPatConfig.reload();
		playerListConfig = PlayerListConfig.getInstance();

		PatPatPacketManager.register();
		PatPatCommandManager.register();

		LOGGER.info("Plugin started");
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
